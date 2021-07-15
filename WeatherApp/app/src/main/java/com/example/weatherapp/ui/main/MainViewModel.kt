package com.example.weatherapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.RepoImpl
import com.example.weatherapp.model.Weather
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> =
        MutableLiveData(),
    private val repositoryImpl: Repo = RepoImpl()

) : ViewModel() {
    fun getLiveData() = liveDataToObserve
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian=true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian:Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(AppState.Success(
                if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                else repositoryImpl.getWeatherFromLocalStorageWorld()))
        }.start()
    }
}
sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
