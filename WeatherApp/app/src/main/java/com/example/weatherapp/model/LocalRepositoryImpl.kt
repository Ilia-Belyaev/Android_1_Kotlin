package com.example.weatherapp.model

import com.example.weatherapp.room.HistoryDao
import com.example.weatherapp.room.HistoryEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) :
    LocalRepository {

    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }

    private fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>):
            List<Weather> {
        return entityList.map {
            Weather(City(it.city, 0.0, 0.0), it.temperature, 0, it.condition)
        }
    }

    private fun convertWeatherToEntity(weather: Weather): HistoryEntity {
        return HistoryEntity(
            0, weather.city.city, weather.temperature,
            weather.condition
        )
    }
}