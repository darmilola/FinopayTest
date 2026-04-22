package com.nrs.finopaytest.ui.weather_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nrs.finopaytest.domain.model.Weather

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherListScreen(
    onCityClick: (String) -> Unit,
    viewModel: WeatherListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weather App") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search city...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            if (state.isLoading && state.weatherItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null && state.weatherItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error ?: "Unknown Error", color = MaterialTheme.colorScheme.error)
                }
            } else {
                val filteredItems = state.weatherItems.filter {
                    it.cityName.contains(state.searchQuery, ignoreCase = true)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredItems) { weather ->
                        WeatherItem(
                            weather = weather,
                            onToggleFavorite = { viewModel.toggleFavorite(weather.cityName) },
                            onClick = { onCityClick(weather.cityName) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherItem(
    weather: Weather,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = weather.cityName, style = MaterialTheme.typography.titleLarge)
            Text(text = "${weather.temperature}°C - ${weather.condition}", style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (weather.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (weather.isFavorite) Color.Red else Color.Gray
            )
        }
    }
}
