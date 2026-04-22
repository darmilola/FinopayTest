package com.nrs.finopaytest.ui.weather_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nrs.finopaytest.domain.model.Weather
import com.nrs.finopaytest.domain.model.WeatherDetailState
import com.nrs.finopaytest.ui.theme.FinopayTestTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherDetailScreen(
    onBackClick: () -> Unit,
    viewModel: WeatherDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    WeatherDetailContent(
        state = state,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailContent(
    state: WeatherDetailState,
    onBackClick: () -> Unit
) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFCC66FF),
            Color(0xFF9933FF)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.weather?.cityName ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(paddingValues)
        ) {
            val error = state.error
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
            } else if (error != null) {
                Text(
                    text = error,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                state.weather?.let { weather ->
                    val dateFormatter = SimpleDateFormat("EEEE | dd MMM yyyy", Locale.getDefault())
                    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val dateString = dateFormatter.format(Date(weather.lastUpdated))
                    val sunriseString = timeFormatter.format(Date(weather.sunrise * 1000))
                    val sunsetString = timeFormatter.format(Date(weather.sunset * 1000))

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = weather.condition,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${weather.temperature.toInt()}°C",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    color = Color.White,
                                    fontSize = 80.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                imageVector = getWeatherIcon(weather.condition),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        Text(
                            text = dateString,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Detail Cards
                        DetailGrid(weather, sunriseString, sunsetString)

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailGrid(weather: Weather, sunrise: String, sunset: String) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            DetailItem("Feels like", "${weather.feelsLike.toInt()}°C", Icons.Default.Thermostat, Modifier.weight(1f))
            DetailItem("Humidity", "${weather.humidity}%", Icons.Default.WaterDrop, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            DetailItem("Wind Speed", "${weather.windSpeed} km/h", Icons.Default.Air, Modifier.weight(1f))
            DetailItem("Description", weather.description.replaceFirstChar { it.uppercase() }, Icons.Default.Info, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            DetailItem("Min / Max", "${weather.tempMin.toInt()}° / ${weather.tempMax.toInt()}°", Icons.Default.VerticalAlignBottom, Modifier.weight(1f))
            DetailItem("Sunrise", sunrise, Icons.Default.WbTwilight, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            DetailItem("Sunset", sunset, Icons.Default.WbTwilight, Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f)))
                Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
            }
        }
    }
}

private fun getWeatherIcon(condition: String): ImageVector {
    return when (condition.lowercase()) {
        "sunny", "clear" -> Icons.Default.WbSunny
        "rainy", "rain" -> Icons.Default.Cloud
        "cloudy" -> Icons.Default.CloudQueue
        "snowy", "snow" -> Icons.Default.AcUnit
        else -> Icons.Default.ModeNight
    }
}

@Preview
@Composable
fun WeatherDetailPreview() {
    FinopayTestTheme {
        WeatherDetailContent(
            state = WeatherDetailState(
                weather = Weather(
                    cityName = "Calabar",
                    temperature = 12.0,
                    condition = "Cloudy",
                    humidity = 80,
                    windSpeed = 10.0,
                    feelsLike = 10.0,
                    tempMin = 8.0,
                    tempMax = 15.0,
                    description = "broken clouds",
                    sunrise = 1661835000L,
                    sunset = 1661880000L
                )
            ),
            onBackClick = {}
        )
    }
}
