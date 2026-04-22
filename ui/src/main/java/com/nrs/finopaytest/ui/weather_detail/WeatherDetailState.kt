package com.nrs.finopaytest.ui.weather_detail

import com.nrs.finopaytest.domain.model.Weather

data class WeatherDetailState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
