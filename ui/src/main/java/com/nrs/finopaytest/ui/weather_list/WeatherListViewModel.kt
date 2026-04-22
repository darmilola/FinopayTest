package com.nrs.finopaytest.ui.weather_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.domain.repository.WeatherRepository
import com.nrs.finopaytest.domain.usecase.GetWeatherListUseCase
import com.nrs.finopaytest.domain.usecase.SearchCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val getWeatherListUseCase: GetWeatherListUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherListState())
    private val _weatherItems = MutableStateFlow<List<com.nrs.finopaytest.domain.model.Weather>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    val state: StateFlow<WeatherListState> = combine(_weatherItems, _searchQuery, _state) { items, query, currentState ->
        currentState.copy(
            weatherItems = searchCitiesUseCase(query, items),
            searchQuery = query
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeatherListState())

    init {
        getWeatherList()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(cityName: String) {
        _weatherItems.update { items ->
            items.map {
                if (it.cityName == cityName) {
                    it.copy(isFavorite = !it.isFavorite)
                } else {
                    it
                }
            }
        }
        
        viewModelScope.launch {
            repository.toggleFavorite(cityName)
        }
    }

    fun addCity(cityName: String, countryCode: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.addCity(cityName, countryCode)
            getWeatherList() // Refresh the list after adding
        }
    }

    fun refresh() {
        getWeatherList()
    }

    private fun getWeatherList() {
        getWeatherListUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _weatherItems.value = result.data ?: emptyList()
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _weatherItems.value = result.data ?: emptyList()
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }
}
