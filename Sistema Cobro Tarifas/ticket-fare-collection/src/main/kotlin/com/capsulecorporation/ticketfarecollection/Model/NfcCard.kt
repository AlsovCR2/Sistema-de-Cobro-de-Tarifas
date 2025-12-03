package com.capsulecorporation.ticketfarecollection.Model

import java.io.Serializable
import java.math.BigDecimal

class NfcCard(var id: String, var balance: BigDecimal) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}