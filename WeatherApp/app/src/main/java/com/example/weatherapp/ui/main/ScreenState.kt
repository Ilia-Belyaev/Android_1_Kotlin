package com.example.weatherapp.ui.main

import com.example.weatherapp.model.Weather

sealed class ScreenState {
    data class Success(val weatherData: List<Weather>) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
    object Loading : ScreenState()
}