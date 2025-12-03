package com.capsulecorporation.ticketfarecollection.Model

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

class FareTransaction(
    var nfcCard: NfcCard,
    var fareAmount: BigDecimal,
    var transactionTime: LocalDateTime
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}