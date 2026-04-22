package com.nrs.finopaytest.domain.usecase

import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.domain.model.Weather
import com.nrs.finopaytest.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetWeatherListUseCaseTest {

    private lateinit var getWeatherListUseCase: GetWeatherListUseCase
    private val repository: WeatherRepository = mockk()

    @Before
    fun setUp() {
        getWeatherListUseCase = GetWeatherListUseCase(repository)
    }

    @Test
    fun `invoke should return weather list from repository`() = runBlocking {
        // Given
        val weatherList = listOf(
            Weather("London", 20.0, "Cloudy", 50, 5.0),
            Weather("Paris", 22.0, "Sunny", 40, 3.0)
        )
        val expectedResource = Resource.Success(weatherList)
        every { repository.getWeatherList() } returns flowOf(expectedResource)

        // When
        val result = getWeatherListUseCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedResource, result[0])
    }
}
