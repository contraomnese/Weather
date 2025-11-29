package com.contraomnese.weather.data.repository

interface TransactionProvider {
    suspend fun <R> runWithTransaction(block: suspend () -> R): R
}