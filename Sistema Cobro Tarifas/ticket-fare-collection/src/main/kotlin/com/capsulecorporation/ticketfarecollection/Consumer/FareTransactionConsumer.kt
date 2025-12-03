package com.capsulecorporation.ticketfarecollection.Consumer

import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import com.capsulecorporation.ticketfarecollection.Service.FareTransactionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class FareTransactionConsumer(
    private val fareTransactionService: FareTransactionService
) {
    @RabbitListener(queues = ["\${rabbitmq.queue.name}"], autoStartup = "false")
    fun receiveTransaction(transaction: FareTransaction) {
        LOGGER.info("Transaccion Recibida con ID -> ${transaction.nfcCard.id}")
        fareTransactionService.processTransaction(transaction)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(FareTransactionConsumer::class.java)
    }
}


