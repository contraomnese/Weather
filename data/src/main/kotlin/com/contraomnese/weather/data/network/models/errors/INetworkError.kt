package com.contraomnese.weather.data.network.models.errors

interface INetworkError {
    val error: NetworkError?
}

interface NetworkError {
    val code: Int?
    val message: String?
}