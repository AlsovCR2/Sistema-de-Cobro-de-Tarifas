import com.capsulecorporation.ticketfarecollection.Consumer.FareTransactionConsumer
import com.capsulecorporation.ticketfarecollection.Handler.FareTransactionHandler
import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import com.capsulecorporation.ticketfarecollection.Model.NfcCard
import com.capsulecorporation.ticketfarecollection.Producer.FareTransactionProducer
import com.capsulecorporation.ticketfarecollection.Service.FareTransactionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime

class FareTransactionIntegrationTest {

    @Test
    fun `create and send transaction successfully`() {
        val nfcCard = NfcCard("123", BigDecimal(100))
        val fareTransaction = FareTransaction(nfcCard, BigDecimal(10), LocalDateTime.now())
        val fareTransactionService = Mockito.mock(FareTransactionService::class.java)
        val fareTransactionProducer = Mockito.mock(FareTransactionProducer::class.java)
        val fareTransactionHandler = FareTransactionHandler(fareTransactionProducer, fareTransactionService)

        val response = fareTransactionHandler.handlerCreateTransaction(fareTransaction)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Transaccion Aceptada.", response.body)
    }

    @Test
    fun `receive and process transaction successfully`() {
        // Arrange
        val nfcCard = NfcCard("123", BigDecimal(100))
        val fareTransaction = FareTransaction(nfcCard, BigDecimal(10), LocalDateTime.now())
        val fareTransactionService = Mockito.mock(FareTransactionService::class.java)
        val fareTransactionConsumer = FareTransactionConsumer(fareTransactionService)

        // Mock the behavior of fareTransactionService to update the card balance
        Mockito.doAnswer { invocation ->
            val transaction = invocation.getArgument<FareTransaction>(0)
            transaction.getNfcCard()
                .setBalance(transaction.getNfcCard().getBalance().subtract(transaction.getFareAmount()))
            null
        }.`when`(fareTransactionService).processTransaction(any())

        // Act
        fareTransactionConsumer.receiveTransaction(fareTransaction)

        // Assert
        val updatedCard = fareTransaction.getNfcCard()
        assertEquals(BigDecimal(90), updatedCard.getBalance())
    }

}