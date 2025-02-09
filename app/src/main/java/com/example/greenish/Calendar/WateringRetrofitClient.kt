package com.example.greenish

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WateringRetrofitClient {
    private const val BASE_URL = "http://ec2-15-164-101-248.ap-northeast-2.compute.amazonaws.com:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val wateringApi: WateringApi = retrofit.create(WateringApi::class.java)
    val plantApi: PlantApi = retrofit.create(PlantApi::class.java)
}