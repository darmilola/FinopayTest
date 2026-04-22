package com.nrs.finopaytest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nrs.finopaytest.ui.weather_detail.WeatherDetailScreen
import com.nrs.finopaytest.ui.weather_list.WeatherListScreen

@Composable
fun WeatherNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.WeatherList.route
    ) {
        composable(route = Screen.WeatherList.route) {
            WeatherListScreen(
                onCityClick = { cityName ->
                    navController.navigate(Screen.WeatherDetail.createRoute(cityName))
                }
            )
        }
        composable(route = Screen.WeatherDetail.route) {
            WeatherDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
