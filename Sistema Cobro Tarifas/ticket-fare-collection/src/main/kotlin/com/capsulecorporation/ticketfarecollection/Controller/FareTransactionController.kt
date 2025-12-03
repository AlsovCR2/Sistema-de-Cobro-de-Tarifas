package com.capsulecorporation.ticketfarecollection.Controller

import com.capsulecorporation.ticketfarecollection.Handler.FareTransactionHandler
import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/transactions")
class FareTransactionController(private val fareTransactionHandler: FareTransactionHandler) {

    @PostMapping("/publisher")
    fun createTransaction(@RequestBody transaction: FareTransaction): ResponseEntity<String> {
        return fareTransactionHandler.handleCreateTransaction(transaction)
    }
}