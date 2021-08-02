package com.example.weatherapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.ui.main.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    //создаём LiveData для передачи данных
    val detailsLiveData: MutableLiveData<ScreenState> = MutableLiveData(),
    //создаем репозиторий для получения данных
    private val detailsRepositoryImpl: DetailsRepository =
        DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    //метод осуществляет запрос на сервер через репозиторий
    fun requestWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = ScreenState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    //здесь обрабатывается полученный ответ от сервера и принимается решение о состоянии экрана
    private val callBack = object : retrofit2.Callback<WeatherDTO> {
        @Throws(IOException::class)
        // Вызывается, если ответ от сервера пришёл
        override fun onResponse(
            call: retrofit2.Call<WeatherDTO>, response:
            retrofit2.Response<WeatherDTO>
        ) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                // Синхронизируем поток с потоком UI
                //  если ответ удачный от 200 до 300 не включая
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    ScreenState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        // Вызывается при сбое в процессе запроса на сервер
        override fun onFailure(call: retrofit2.Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(
                ScreenState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }

        //проверяем ответ
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