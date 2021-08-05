package com.example.weatherapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.app.App.Companion.getHistoryDao
import com.example.weatherapp.ui.main.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<ScreenState> = MutableLiveData(),
    private val detailsRepository: DetailsRepository =
        DetailsRepositoryImpl(RemoteDataSource()),
    private val historyRepository: LocalRepository =
        LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {
    fun requestWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = ScreenState.Loading
        detailsRepository.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    fun saveCityToDB(weather: Weather) {
        historyRepository.saveEntity(weather)
    }

    private val callBack = object : Callback<WeatherDTO> {
        @Throws(IOException::class)
        override fun onResponse(
            call: Call<WeatherDTO>, response:
            Response<WeatherDTO>
        ) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    ScreenState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(
                ScreenState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }

        private fun checkResponse(serverResponse: WeatherDTO): ScreenState {
            val fact = serverResponse.factInfo
            return if (fact?.temperature == null || fact.feels_like == null ||
                fact.condition.isNullOrEmpty()
            ) {
                ScreenState.Error(Throwable(CORRUPTED_DATA))
            } else {
                ScreenState.Success(convertDtoToModel(serverResponse))
            }
        }
    }
}