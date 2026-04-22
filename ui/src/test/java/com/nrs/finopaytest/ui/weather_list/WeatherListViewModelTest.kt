package com.nrs.finopaytest.ui.weather_list

import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.domain.model.Weather
import com.nrs.finopaytest.domain.repository.WeatherRepository
import com.nrs.finopaytest.domain.usecase.GetWeatherListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherListViewModelTest {

    private lateinit var viewModel: WeatherListViewModel
    private val getWeatherListUseCase: GetWeatherListUseCase = mockk()
    private val repository: WeatherRepository = mockk()
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        val weatherList = listOf(Weather("London", 20.0, "Cloudy", 50, 5.0))
        every { getWeatherListUseCase() } returns flowOf(Resource.Success(weatherList))
        
        viewModel = WeatherListViewModel(getWeatherListUseCase, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should load weather items`() = runTest {
        val state = viewModel.state.value
        assertEquals(1, state.weatherItems.size)
        assertEquals("London", state.weatherItems[0].cityName)
    }

    @Test
    fun `onSearchQueryChange should update state`() {
        viewModel.onSearchQueryChange("Paris")
        assertEquals("Paris", viewModel.state.value.searchQuery)
    }
}
