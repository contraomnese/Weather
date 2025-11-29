package com.contraomnese.weather.data

import com.contraomnese.weather.data.repository.TransactionProvider

class FakeTransactionProvider : TransactionProvider {
    override suspend fun <R> runWithTransaction(block: suspend () -> R): R {
        return block()
    }
}