package com.nrs.finopaytest.domain.repository

import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherList(): Flow<Resource<List<Weather>>>
    fun getWeatherDetail(cityName: String): Flow<Resource<Weather>>
    suspend fun toggleFavorite(cityName: String)
    suspend fun refreshWeather()
}
