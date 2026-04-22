package com.nrs.finopaytest.ui.weather_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrs.finopaytest.core.Resource
import com.nrs.finopaytest.domain.model.WeatherDetailState
import com.nrs.finopaytest.domain.usecase.GetWeatherDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val getWeatherDetailUseCase: GetWeatherDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherDetailState())
    val state: StateFlow<WeatherDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("cityName")?.let { cityName ->
            getWeatherDetail(cityName)
        }
    }

    private fun getWeatherDetail(cityName: String) {
        getWeatherDetailUseCase(cityName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { 
                        it.copy(weather = result.data, isLoading = false, error = null)
                    }
                }
                is Resource.Error -> {
                    _state.update { 
                        it.copy(isLoading = false, error = result.message)
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }
}
