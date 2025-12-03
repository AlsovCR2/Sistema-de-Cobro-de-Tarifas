package com.capsulecorporation.ticketfarecollection

import com.capsulecorporation.ticketfarecollection.Consumer.FareTransactionConsumer
import com.capsulecorporation.ticketfarecollection.Database.LocalDatabase
import com.capsulecorporation.ticketfarecollection.Handler.FareTransactionHandler
import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import com.capsulecorporation.ticketfarecollection.Model.NfcCard
import jakarta.annotation.PostConstruct
import org.springframework.amqp.AmqpIOException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootApplication
@EnableScheduling
class TicketFareCollectionApplication {
    @Autowired
    private lateinit var fareTransactionHandler: FareTransactionHandler
    @Autowired
    private lateinit var fareTransactionConsumer: FareTransactionConsumer
    @Autowired
    private lateinit var localDatabase: LocalDatabase

    private val executorService = Executors.newSingleThreadScheduledExecutor()

    @PostConstruct
    fun run() {
        executorService.scheduleAtFixedRate(::sendTransactions, 0, 1, TimeUnit.SECONDS)
        executorService.scheduleAtFixedRate(::processTransactions, 0, 1, TimeUnit.SECONDS)
    }

    private fun sendTransactions() {
        for (i in 0 until 1) {
            val cardId = UUID.randomUUID().toString()
            val card = NfcCard(cardId, BigDecimal.TEN)
            val transactionTime = LocalDateTime.now().plusSeconds(1)
            val transaction = FareTransaction(card, BigDecimal.ONE, transactionTime)

            try {
                fareTransactionHandler.handleCreateTransaction(transaction)
            } catch (e: AmqpIOException) {
                localDatabase.saveTransaction(transaction)
            }
        }
    }

    private fun processTransactions() {
        val transactions = localDatabase.getTransactions()
        for (transaction in transactions) {
            fareTransactionConsumer.receiveTransaction(transaction)
            localDatabase.removeTransaction(transaction)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TicketFareCollectionApplication::class.java, *args)
        }
    }
}