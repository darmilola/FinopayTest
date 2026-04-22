package com.nrs.finopaytest.ui.city_management

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nrs.finopaytest.domain.model.Weather
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityManagementScreen(
    cities: List<Weather>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onAddCity: (String, String) -> Unit,
    onCityClick: (Weather) -> Unit,
    onFavoriteClick: (String) -> Unit,
    isLoading: Boolean = false,
    error: String? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddCityDialog(
            onDismiss = { showDialog = false },
            onConfirm = { cityName, countryCode ->
                onAddCity(cityName, countryCode)
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "City Weather Management",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search city...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.1f),
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.1f)
                )
            )

            if (isLoading && cities.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null && cities.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cities) { city ->
                        CityCard(
                            weather = city,
                            onClick = { onCityClick(city) },
                            onFavoriteClick = { onFavoriteClick(city.cityName) }
                        )
                    }
                    item {
                        AddCityCard(onClick = { showDialog = true })
                    }
                }
            }
        }
    }
}

@Composable
fun AddCityDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var cityName by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New City") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    label = { Text("City Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = countryCode,
                    onValueChange = { countryCode = it },
                    label = { Text("Country Code (e.g. NG, GB)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(cityName, countryCode) },
                enabled = cityName.isNotBlank() && countryCode.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CityCard(
    weather: Weather,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val (gradient, icon) = when (weather.condition.lowercase()) {
        "sunny", "clear" -> Pair(
            Brush.verticalGradient(listOf(Color(0xFFFFB74D), Color(0xFFFF8A65))),
            Icons.Default.WbSunny
        )
        "rainy", "rain" -> Pair(
            Brush.verticalGradient(listOf(Color(0xFF81C784), Color(0xFF4DB6AC))),
            Icons.Default.Cloud
        )
        "cloudy" -> Pair(
            Brush.verticalGradient(listOf(Color(0xFF9575CD), Color(0xFF7E57C2))),
            Icons.Default.CloudQueue
        )
        "snowy", "snow" -> Pair(
            Brush.verticalGradient(listOf(Color(0xFFF06292), Color(0xFFBA68C8))),
            Icons.Default.AcUnit
        )
        else -> Pair(
            Brush.verticalGradient(listOf(Color(0xFF64B5F6), Color(0xFF3F51B5))),
            Icons.Default.ModeNight
        )
    }

    val dateFormatter = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val dateString = dateFormatter.format(Date(weather.lastUpdated))

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                IconButton(
                    onClick = { onFavoriteClick() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (weather.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (weather.isFavorite) Color.Red else Color.White
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(40.dp)
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = "${weather.temperature.toInt()}°C",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        fontSize = 32.sp
                    )
                )
                Text(
                    text = weather.cityName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun AddCityCard(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Color(0xFFFFA726),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Add New City",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityManagementScreenPreview() {
    val dummyCities = listOf(
        Weather("London", 15.0, "Cloudy", 80, 10.0, isFavorite = true),
        Weather("New York", 22.0, "Sunny", 60, 5.0, isFavorite = false),
        Weather("Tokyo", 18.0, "Rainy", 90, 15.0, isFavorite = true),
        Weather("Paris", 12.0, "Snowy", 70, 8.0, isFavorite = false)
    )
    CityManagementScreen(
        cities = dummyCities,
        searchQuery = "",
        onSearchQueryChange = {},
        onBackClick = {},
        onAddCity = { _, _ -> },
        onCityClick = {},
        onFavoriteClick = {}
    )
}
