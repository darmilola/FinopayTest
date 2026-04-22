package com.nrs.finopaytest.repository

import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.domain.model.Weather
import com.nrs.finopaytest.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FakeWeatherRepository @Inject constructor() : WeatherRepository {

    private val weatherList = MutableStateFlow<List<Weather>>(
        listOf(
            Weather(
                cityName = "Lagos",
                temperature = 30.0,
                condition = "Sunny",
                humidity = 70,
                windSpeed = 5.0,
                feelsLike = 32.0,
                tempMin = 28.0,
                tempMax = 34.0,
                description = "clear sky",
                sunrise = 1661835000L,
                sunset = 1661880000L,
                isFavorite = true
            )
        )
    )

    override fun getWeatherList(): Flow<Resource<List<Weather>>> {
        return weatherList.map { Resource.Success(it) }
    }

    override fun getWeatherDetail(cityName: String): Flow<Resource<Weather>> {
        return weatherList.map { list ->
            val weather = list.find { it.cityName == cityName }
            if (weather != null) Resource.Success(weather)
            else Resource.Error("City not found")
        }
    }

    override suspend fun toggleFavorite(cityName: String) {
        val currentList = weatherList.value.toMutableList()
        val index = currentList.indexOfFirst { it.cityName == cityName }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isFavorite = !currentList[index].isFavorite)
            weatherList.value = currentList
        }
    }

    override suspend fun refreshWeather() {
        // No-op for fake
    }

    override suspend fun addCity(cityName: String, countryCode: String) {
        val currentList = weatherList.value.toMutableList()
        if (currentList.none { it.cityName == cityName }) {
            currentList.add(
                Weather(
                    cityName = cityName,
                    temperature = 20.0,
                    condition = "Cloudy",
                    humidity = 60,
                    windSpeed = 10.0,
                    feelsLike = 18.0,
                    tempMin = 15.0,
                    tempMax = 22.0,
                    description = "broken clouds",
                    sunrise = 1661835000L,
                    sunset = 1661880000L
                )
            )
            weatherList.value = currentList
        }
    }

    override suspend fun getAnyFavoriteCity(): Weather? {
        return weatherList.value.find { it.isFavorite }
    }
}
