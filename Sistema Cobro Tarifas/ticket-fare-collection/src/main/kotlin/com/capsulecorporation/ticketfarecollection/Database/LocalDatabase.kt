package com.capsulecorporation.ticketfarecollection.Database

import com.capsulecorporation.ticketfarecollection.Model.FareTransaction

class LocalDatabase {
    // Lista para almacenar las transacciones
    private val transactions = mutableListOf<FareTransaction>()

    // Método para guardar una transacción en la base de datos
    fun saveTransaction(transaction: FareTransaction) {
        transactions.add(transaction)
    }

    // Método para obtener todas las transacciones almacenadas
    fun getTransactions(): List<FareTransaction> {
        return transactions
    }

    // Método para borrar todas las transacciones
    fun clearTransactions() {
        transactions.clear()
    }

    // Método para eliminar una transacción específica
    fun removeTransaction(transaction: FareTransaction) {
        transactions.remove(transaction)
    }
}
