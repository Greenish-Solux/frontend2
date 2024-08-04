package com.example.greenish.StartServer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SignupRequestToServer {
    var retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-15-164-101-248.ap-northeast-2.compute.amazonaws.com:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: RequestSignupInterface = retrofit.create(RequestSignupInterface::class.java)
}