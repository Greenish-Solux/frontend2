package com.example.greenish.SearchpageANDMypage

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @PUT("environment/update-location")
    suspend fun sendLocation(
        @Header("Authorization") token: String,
        @Body locationData: LocationData
    ): Response<Unit>

    @GET("user-info")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserInfo>

    @PUT("update-profile-image")
    @Multipart
    suspend fun updateProfileImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Response<ProfileImageResponse>

    data class ProfileImageResponse(
        val userId: String,
        val photoUrl: String
    )

    @GET("alarm")
    suspend fun getAlarmSettings(@Header("Authorization") token: String): Response<AlarmSettings>

    @PUT("alarm/update")
    suspend fun updateAlarmSettings(@Header("Authorization") token: String, @Body settings: AlarmSettings): Response<Unit>

    data class AlarmSettings(
        val all: Boolean,
        val hapticMode: Boolean,
        val preview: Boolean,
        val allPlantWatering: Boolean
    )

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @DELETE("delete-account")
    suspend fun deleteAccount(@Header("Authorization") token: String): Response<Unit>

    @GET("ranking")
    suspend fun getRanking(@Header("Authorization") token: String): Response<RankingResponse>

    data class RankingResponse(
        val myRank: RankingUser,
        val topUsers: List<RankingUser>
    )

    data class RankingUser(
        val userId: Int,
        val nickname: String,
        val rank: Int,
        val recordCount: Int,
        val profileImageUrl: String
    )
}

