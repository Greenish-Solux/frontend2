package com.example.greenish.StartServer

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RequestSignupInterface {

    @POST("/signUp")
    fun requestSignup(@Body body : RequestSignup) : Call<ResponseSignup>

    @GET("/check-email")
    fun requestDuplicateEmail(@Body body : RequestDuplicateEmail) : Call<ResponseDuplicateEmail>

    @GET("/check-nickname")
    fun requestDuplicateNickname(@Body body : RequestDuplicateNickname) : Call<ResponseDuplicateNickname>

}