package com.capsulecorporation.ticketfarecollection.Producer

import com.capsulecorporation.ticketfarecollection.Database.LocalDatabase
import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import com.capsulecorporation.ticketfarecollection.Service.RabbitMQListenerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpConnectException
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.retry.support.RetryTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

@Service
class FareTransactionProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val localDatabase: LocalDatabase,
    private val rabbitMQListenerService: RabbitMQListenerService,
    private val retryTemplate: RetryTemplate
) {
    @Value("\${rabbitmq.exchange.name}")
    private lateinit var exchange: String

    @Value("\${rabbitmq.routing.key}")
    private lateinit var routingKey: String

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(FareTransactionProducer::class.java)
    }

    fun sendTransactionWithFallback(transaction: FareTransaction) {
        try {
            retryTemplate.execute<Void, Exception> {
                rabbitTemplate.convertAndSend(exchange, routingKey, transaction)
                null
            }
            LOGGER.info("Transaccion enviada con ID-> ${transaction.nfcCard.id}")
        } catch (e: AmqpConnectException) {
            LOGGER.error("Fallo en conectar con RabbitMQ", e)
            localDatabase.saveTransaction(transaction)
            LOGGER.info("Transaccion guardada localmente, debido a la falta de conexion, ID-> ${transaction.nfcCard.id}")
        }
    }

    @Scheduled(fixedDelay = 60000)
    @Synchronized
    fun syncTransactionsWithCentralSystem() {
        if (isConnectionAvailable()) {
            rabbitMQListenerService.startListeners()
            val transactions = ArrayList(localDatabase.getTransactions())
            localDatabase.clearTransactions()
            transactions.forEach { sendTransactionWithFallback(it) }
        }
    }

    private fun isConnectionAvailable(): Boolean {
        return try {
            Socket().use { it.connect(InetSocketAddress("google.com", 80), 2000) }
            true
        } catch (e: IOException) {
            false
        }
    }
}