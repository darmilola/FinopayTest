package com.nrs.finopaytest.domain.usecase

import com.nrs.finopaytest.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherListUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke() = repository.getWeatherList()
}
