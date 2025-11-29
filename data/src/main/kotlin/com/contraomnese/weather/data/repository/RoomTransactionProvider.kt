package com.contraomnese.weather.data.repository

import androidx.room.RoomDatabase
import androidx.room.withTransaction

class RoomTransactionProvider(private val db: RoomDatabase) : TransactionProvider {
    override suspend fun <R> runWithTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}