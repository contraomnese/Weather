package com.contraomnese.weather.data.storage.db.locations.dto

import androidx.room.ColumnInfo

data class LocationDto(
    @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = COUNTRY_ID) val countryId: Int,
    @ColumnInfo(name = COUNTRY_NAME) val countryName: String,
    @ColumnInfo(name = IS_FAVORITE) val isFavorite: Boolean,
) {
    companion object {
        const val ID = "alias_id"
        const val NAME = "alias_name"
        const val COUNTRY_ID = "alias_country_id"
        const val COUNTRY_NAME = "alias_country_name"
        const val IS_FAVORITE = "alias_is_favorite"
    }
}