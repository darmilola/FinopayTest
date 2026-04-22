package com.nrs.finopaytest.data.mapper

import com.nrs.finopaytest.data.local.entity.WeatherEntity
import com.nrs.finopaytest.data.remote.dto.WeatherResponse
import com.nrs.finopaytest.domain.model.Weather

fun WeatherResponse.toWeatherEntity(): WeatherEntity {
    return WeatherEntity(
        cityName = name,
        temperature = main.temp,
        condition = weather.firstOrNull()?.main ?: "Unknown",
        humidity = main.humidity,
        windSpeed = wind.speed
    )
}

fun WeatherEntity.toWeather(): Weather {
    return Weather(
        cityName = cityName,
        temperature = temperature,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed,
        isFavorite = isFavorite
    )
}
