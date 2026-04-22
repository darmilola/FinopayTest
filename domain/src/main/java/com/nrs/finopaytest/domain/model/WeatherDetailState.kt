package com.nrs.finopaytest.domain.model

data class WeatherDetailState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)