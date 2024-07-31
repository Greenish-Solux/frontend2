package com.example.greenish

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("plantsearch")
    fun searchPlants(@Query("name") name: String): Call<PlantResponse>

    @GET("path/{id}")
    fun getPlantData(@Path("id") id: String): Call<SearchResult>

    @GET("plantsearch/filter")
    fun searchPlantsWithFilter(@QueryMap filters: Map<String, String>): Call<PlantResponse>

    // 다른 메서드들은 그대로 유지

    @GET("plantsearch/plant/{cntntsNo}")
    suspend fun getPlantDetails(@Path("cntntsNo") cntntsNo: String): Response<PlantDetails>

    @POST("location")  // 실제 엔드포인트로 변경해주세요
    suspend fun sendLocation(@Body locationData: LocationData): Response<Unit>
}
