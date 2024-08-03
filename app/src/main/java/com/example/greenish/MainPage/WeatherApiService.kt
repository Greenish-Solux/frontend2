package com.example.greenish.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface WeatherApiService {
    @GET("weather/forecast")
    fun getWeatherForecast(@Header("Authorization") token: String): Call<List<TemperatureResponse>>
}