package com.example.greenish.StartServer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @POST("/signUp")
    fun userRegister(
        @Body userData: RegisterModel
    ): Call<RegisterBackendResponse>

    @POST("/login")
    fun userLogin(
        @Body loginData: LoginModel
    ): Call<LoginBackendResponse>

    @GET("/check-nickname")
    fun checkNickname(
        @Query("nickname") nickname: String
    ): Call<NicknameCheckResponse>

    companion object {
        private const val BASE_URL = "http://ec2-15-164-101-248.ap-northeast-2.compute.amazonaws.com:8080/"
        val gson: Gson = GsonBuilder().setLenient().create()

        fun create(): Api {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(Api::class.java)
        }
    }
}
