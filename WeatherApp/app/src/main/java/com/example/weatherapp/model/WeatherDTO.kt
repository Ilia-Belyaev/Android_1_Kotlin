package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val factInfo: FactDTO?
)

data class FactDTO(
    @SerializedName("temp")
    val temperature: Int?,
    val feels_like: Int?,
    val condition: String?,
    val icon: String?
)

