package com.nrs.finopaytest.data.repository

import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.data.BuildConfig
import com.nrs.finopaytest.data.local.WeatherDao
import com.nrs.finopaytest.data.mapper.toWeather
import com.nrs.finopaytest.data.mapper.toWeatherEntity
import com.nrs.finopaytest.data.remote.WeatherApiService
import com.nrs.finopaytest.domain.model.City
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

    private val apiKey = BuildConfig.API_KEY

    override fun getWeatherList(): Flow<Resource<List<Weather>>> = flow {
        emit(Resource.Loading())
        
        val localEntities = dao.getWeatherList().first()
        val localWeather = localEntities.map { it.toWeather() }
        
        if (localWeather.isNotEmpty()) {
            emit(Resource.Success(localWeather))
        }

        try {
            refreshWeather()
            val updatedWeather = dao.getWeatherList().first().map { it.toWeather() }
            emit(Resource.Success(updatedWeather))
        } catch (e: Exception) {
            if (localWeather.isEmpty()) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            } else {
                emit(Resource.Success(localWeather))
            }
        }
    }

    override fun getWeatherDetail(cityName: String): Flow<Resource<Weather>> = flow {
        emit(Resource.Loading())
        val entity = dao.getWeatherDetail(cityName).first()
        if (entity != null) {
            emit(Resource.Success(entity.toWeather()))
        } else {
            emit(Resource.Error("City not found"))
        }
    }

    override suspend fun toggleFavorite(cityName: String) {
        dao.toggleFavorite(cityName)
    }

    override suspend fun refreshWeather() {
        val entities = City.entries.map { city ->
            val query = "${city.cityName},${city.countryCode}"
            val response = api.getWeather(query, apiKey)
            response.toWeatherEntity()
        }
        
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

    override suspend fun addCity(cityName: String, countryCode: String) {
        try {
            val query = "$cityName,$countryCode"
            val response = api.getWeather(query, apiKey)
            val entity = response.toWeatherEntity()
            dao.insertWeatherList(listOf(entity))
        } catch (e: Exception) {
            // Handle error
        }
    }
}
