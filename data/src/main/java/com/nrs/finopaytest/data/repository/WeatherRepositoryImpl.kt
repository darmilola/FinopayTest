package com.nrs.finopaytest.data.repository

import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.data.local.WeatherDao
import com.nrs.finopaytest.data.mapper.toWeather
import com.nrs.finopaytest.data.mapper.toWeatherEntity
import com.nrs.finopaytest.data.remote.WeatherApiService
import com.nrs.finopaytest.domain.model.Weather
import com.nrs.finopaytest.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val dao: WeatherDao
) : WeatherRepository {

    private val apiKey = "YOUR_API_KEY" // In a real app, this would be in local.properties or a secrets manager
    private val defaultCities = listOf("London", "New York", "Tokyo", "Paris", "Berlin", "Lagos", "Dubai", "Sydney")

    override fun getWeatherList(): Flow<Resource<List<Weather>>> = flow {
        emit(Resource.Loading())
        
        val localWeather = dao.getWeatherList().first().map { it.toWeather() }
        if (localWeather.isNotEmpty()) {
            emit(Resource.Success(localWeather))
        }

        try {
            refreshWeather()
            val updatedWeather = dao.getWeatherList().first().map { it.toWeather() }
            emit(Resource.Success(updatedWeather))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection.", localWeather))
        }
    }

    override fun getWeatherDetail(cityName: String): Flow<Resource<Weather>> = flow {
        emit(Resource.Loading())
        
        dao.getWeatherDetail(cityName).collect { entity ->
            if (entity != null) {
                emit(Resource.Success(entity.toWeather()))
            } else {
                emit(Resource.Error("City not found"))
            }
        }
    }

    override suspend fun toggleFavorite(cityName: String) {
        dao.toggleFavorite(cityName)
    }

    override suspend fun refreshWeather() {
        // In a production app, we'd fetch for all cities or just visible ones
        val entities = defaultCities.map { city ->
            val response = api.getWeather(city, apiKey)
            response.toWeatherEntity()
        }
        
        // Preserve favorite status during refresh
        val favorites = dao.getFavoriteCityNames()
        val finalEntities = entities.map { entity ->
            if (favorites.contains(entity.cityName)) {
                entity.copy(isFavorite = true)
            } else {
                entity
            }
        }
        
        dao.insertWeatherList(finalEntities)
    }
}
