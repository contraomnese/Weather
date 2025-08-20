package com.contraomnese.weather.data.network.models

data class ErrorResponse(
    val error: ErrorDetail,
)

data class ErrorDetail(
    val code: Int,
    val message: String,
)