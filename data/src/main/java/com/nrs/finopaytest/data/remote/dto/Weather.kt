package com.nrs.finopaytest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val name: String,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<WeatherDescription>,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("sys") val sys: Sys
)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherDescription(
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String
)

data class Wind(
    @SerializedName("speed") val speed: Double
)

data class Sys(
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)
