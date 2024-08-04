package com.example.greenish

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WateringApi {
    @GET("waterings/date/{date}")
    suspend fun getWateringSchedule(
        @Header("Authorization") token: String,
        @Path("date") date: String
    ): WateringResponse

    @PUT("waterings/complete/{watering_id}")
    suspend fun completeWatering(
        @Header("Authorization") token: String,
        @Path("watering_id") wateringId: Int
    ): WateringCompleteResponse

    @GET("waterings/user")
    suspend fun getUserWaterings(@Header("Authorization") token: String): WateringResponse
}