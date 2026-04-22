package com.nrs.finopaytest.ui.weather_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.nrs.finopaytest.ui.city_management.CityManagementScreen

@Composable
fun WeatherListScreen(
    onCityClick: (String) -> Unit,
    viewModel: WeatherListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    CityManagementScreen(
        cities = state.weatherItems,
        searchQuery = state.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onBackClick = { /* Handle navigation back */ },
        onAddCity = viewModel::addCity,
        onCityClick = { weather -> onCityClick(weather.cityName) },
        onFavoriteClick = viewModel::toggleFavorite,
        isLoading = state.isLoading,
        error = state.error
    )
}
