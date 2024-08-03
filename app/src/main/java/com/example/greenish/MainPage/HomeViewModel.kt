package com.example.greenish.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenish.retrofit.TemperatureResponse
import com.example.greenish.retrofit.RetrofitClient.weatherApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<List<TemperatureResponse>>()
    val weatherData: LiveData<List<TemperatureResponse>> = _weatherData

    init {
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            weatherApiService.getWeatherForecast("Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3MDk5NDksImV4cCI6MTcyMjcxMzU0OX0.M4otbCF5e55aRCejeXxvx-J4KABkMf7_jvpG9cw1LCI")
                .enqueue(object : Callback<List<TemperatureResponse>> {
                    override fun onResponse(
                        call: Call<List<TemperatureResponse>>,
                        response: Response<List<TemperatureResponse>>
                    ) {
                        if (response.isSuccessful) {
                            _weatherData.value = response.body() ?: emptyList()
                        } else {
                            // Handle API error
                        }
                    }

                    override fun onFailure(call: Call<List<TemperatureResponse>>, t: Throwable) {
                        // Handle network failure
                    }
                })
        }
    }
}