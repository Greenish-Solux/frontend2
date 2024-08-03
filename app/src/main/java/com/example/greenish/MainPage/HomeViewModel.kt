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
            weatherApiService.getWeatherForecast("Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsNkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNzE1Nzc2LCJleHAiOjE3MjI3MTkzNzZ9.xEVSUJY0JXSB1LZ1bz5Ul7jCftKa-3ZvB4H4lnBbdXY")
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