package com.example.greenish.StartServer

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/signUp")
    suspend fun signUp(@Body user: User): Response<SignUpResponse>

    @GET("/check-email")
    suspend fun checkEmail(@Query("email") email: String): Response<Boolean>

    @GET("/check-nickname")
    suspend fun checkNickname(@Query("nickname") nickname: String): Response<Boolean>

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Void>
}