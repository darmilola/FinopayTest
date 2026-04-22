package com.nrs.finopaytest.domain.usecase

import com.nrs.finopaytest.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherDetailUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(cityName: String) = repository.getWeatherDetail(cityName)
}
