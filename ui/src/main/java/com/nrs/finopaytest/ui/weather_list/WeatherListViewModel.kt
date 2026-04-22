package com.nrs.finopaytest.ui.weather_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.domain.repository.WeatherRepository
import com.nrs.finopaytest.domain.usecase.GetWeatherListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val getWeatherListUseCase: GetWeatherListUseCase,
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherListState())
    val state: StateFlow<WeatherListState> = _state.asStateFlow()

    init {
        getWeatherList()
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun toggleFavorite(cityName: String) {
        viewModelScope.launch {
            repository.toggleFavorite(cityName)
        }
    }

    fun refresh() {
        getWeatherList()
    }

    private fun getWeatherList() {
        getWeatherListUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { 
                        it.copy(
                            weatherItems = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { 
                        it.copy(
                            weatherItems = result.data ?: emptyList(),
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
