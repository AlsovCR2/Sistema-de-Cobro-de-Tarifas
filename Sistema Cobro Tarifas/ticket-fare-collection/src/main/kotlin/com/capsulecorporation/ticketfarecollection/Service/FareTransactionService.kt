package com.capsulecorporation.ticketfarecollection.Service

import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import com.capsulecorporation.ticketfarecollection.Model.NfcCard
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class FareTransactionService {
    private val nfcCards = ConcurrentHashMap<String, NfcCard>()

    fun addCard(card: NfcCard) {
        nfcCards[card.id] = card
    }

    fun getCard(id: String): NfcCard? {
        return nfcCards[id]
    }

    fun processTransaction(transaction: FareTransaction) {
        val card = nfcCards[transaction.nfcCard.id]
        card?.let {
            it.balance = it.balance.subtract(transaction.fareAmount)
            LOGGER.info("Transaccion procesada con exito. Saldo al iniciar: ${transaction.nfcCard.balance}. Monto de la tarifa: ${transaction.fareAmount}. Nuevo saldo de la tarjeta: ${it.balance}")
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(FareTransactionService::class.java)
    }
}