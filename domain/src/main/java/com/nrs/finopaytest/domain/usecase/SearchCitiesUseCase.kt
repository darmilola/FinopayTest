package com.nrs.finopaytest.domain.usecase

import com.nrs.finopaytest.domain.model.Weather
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor() {
    operator fun invoke(query: String, cities: List<Weather>): List<Weather> {
        if (query.isBlank()) {
            return cities
        }
        return cities.filter {
            it.cityName.contains(query, ignoreCase = true)
        }
    }
}
