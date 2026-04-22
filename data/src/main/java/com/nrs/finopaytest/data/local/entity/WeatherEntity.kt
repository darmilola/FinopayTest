package com.nrs.finopaytest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val cityName: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
