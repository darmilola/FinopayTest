package com.nrs.finopaytest.data.local

import androidx.room.*
import com.nrs.finopaytest.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY isFavorite DESC, cityName ASC")
    fun getWeatherList(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather WHERE cityName = :cityName")
    fun getWeatherDetail(cityName: String): Flow<WeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherList(weatherList: List<WeatherEntity>)

    @Query("UPDATE weather SET isFavorite = NOT isFavorite WHERE cityName = :cityName")
    suspend fun toggleFavorite(cityName: String)

    @Query("SELECT cityName FROM weather WHERE isFavorite = 1")
    suspend fun getFavoriteCityNames(): List<String>
    
    @Query("SELECT * FROM weather WHERE isFavorite = 1 LIMIT 1")
    suspend fun getAnyFavoriteCity(): WeatherEntity?
}
