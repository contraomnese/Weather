package com.contraomnese.weather.data

import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.generateFake

object MatchingLocationDataFixtures {

    fun generateRandom() = generateFake(MatchingLocationEntity::class) as MatchingLocationEntity

}