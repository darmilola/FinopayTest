package com.nrs.finopaytest.ui.weather_list

import com.nrs.finopaytest.domain.model.Weather

data class WeatherListState(
    val weatherItems: List<Weather> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)
