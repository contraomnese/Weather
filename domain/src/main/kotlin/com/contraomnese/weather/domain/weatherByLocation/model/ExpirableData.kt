package com.contraomnese.weather.domain.weatherByLocation.model

interface ExpirableData {
    val updateTargetId: Int
    val expiresAt: Long
}