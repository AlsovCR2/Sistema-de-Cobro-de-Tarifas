package com.capsulecorporation.ticketfarecollection.Handler

import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import com.capsulecorporation.ticketfarecollection.Producer.FareTransactionProducer
import com.capsulecorporation.ticketfarecollection.Service.FareTransactionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class FareTransactionHandler(
    private val producer: FareTransactionProducer,
    private val fareTransactionService: FareTransactionService
) {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(FareTransactionHandler::class.java)
    }

    fun handleCreateTransaction(transaction: FareTransaction): ResponseEntity<String> {
        val card = transaction.nfcCard

        if (card == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarjeta invalida: ${transaction.nfcCard?.id}")
        }

        val newBalance = card.balance.subtract(transaction.fareAmount)

        if (newBalance < BigDecimal.ZERO) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente: ${transaction.nfcCard?.balance}. La tarifa es de : ${transaction.fareAmount}")
        }

        fareTransactionService.addCard(transaction.nfcCard)
        producer.sendTransactionWithFallback(transaction)
        LOGGER.info("Transaccion Aceptada ID -> ${transaction.nfcCard?.id}")
        return ResponseEntity.ok("Transaccion Aceptada.")
    }
}