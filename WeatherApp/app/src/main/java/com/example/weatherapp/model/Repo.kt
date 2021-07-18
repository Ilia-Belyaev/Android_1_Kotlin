package com.example.weatherapp.model

interface Repo {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageRus()= getRussianCities()
    fun getWeatherFromLocalStorageWorld()= getWorldCities()

}