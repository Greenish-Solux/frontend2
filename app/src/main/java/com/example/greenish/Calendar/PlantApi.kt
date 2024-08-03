package com.example.greenish

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
import retrofit2.http.Url

interface PlantApi {
    @POST("plants")
    suspend fun registerPlant(
        @Header("Authorization") token: String,
        @Body plant: PlantRequest
    ): Response<PlantResponsez>

    @Multipart
    @PUT
    suspend fun uploadPlantImage(
        @Url url: String,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @GET("plants/user")
    suspend fun getPlantList(
        @Header("Authorization") token: String
    ): Response<PlantListResponse>

    @GET("plants/{plant_id}")
    suspend fun getPlantDetail(
        @Header("Authorization") token: String,
        @Path("plant_id") plantId: Int
    ): Response<PlantDetailResponsez>

    @DELETE("plants/{plant_id}")
    suspend fun deletePlant(
        @Header("Authorization") token: String,
        @Path("plant_id") plantId: Int
    ): Response<DeletePlantResponse>

    @POST("posts")
    suspend fun createDiary(@Header("Authorization") token: String, @Body request: CreateDiaryRequest): Response<CreateDiaryResponse>

    @GET("posts/plant/{plant_id}")
    suspend fun getDiaryPosts(
        @Header("Authorization") token: String,
        @Path("plant_id") plantId: Int
    ): Response<DiaryPostsResponse>

    @GET("posts/{postId}")
    suspend fun getDiaryPost(@Header("Authorization") token: String, @Path("postId") postId: Int): Response<DiaryPostResponse>

}