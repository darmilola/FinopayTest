package com.nrs.finopaytest.domain.model

data class Weather(
    val cityName: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val isFavorite: Boolean = false
)
