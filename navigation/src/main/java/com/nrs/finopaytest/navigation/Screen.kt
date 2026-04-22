package com.nrs.finopaytest.navigation

sealed class Screen(val route: String) {
    object WeatherList : Screen("weather_list")
    object WeatherDetail : Screen("weather_detail/{cityName}") {
        fun createRoute(cityName: String) = "weather_detail/$cityName"
    }
}
