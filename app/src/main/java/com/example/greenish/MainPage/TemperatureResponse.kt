package com.example.greenish.retrofit

data class TemperatureResponse(
    val date: String,
    val environment: Environment,
    val humidity: Int,
    val maxTemp: Double,
    val minTemp: Double,
    val weatherCode: String
) {
    data class Environment(
        val id: Int
    )
}