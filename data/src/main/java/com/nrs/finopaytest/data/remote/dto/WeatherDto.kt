package com.nrs.finopaytest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val name: String,
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherDescriptionDto>,
    @SerializedName("wind") val wind: WindDto
)

data class MainDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherDescriptionDto(
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String
)

data class WindDto(
    @SerializedName("speed") val speed: Double
)
