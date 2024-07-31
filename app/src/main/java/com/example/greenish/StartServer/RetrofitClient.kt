package com.example.greenish.StartServer

import android.content.Context
import com.example.greenish.StartPage.MyApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://ec2-15-164-101-248.ap-northeast-2.compute.amazonaws.com:8080/"

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val sharedPreferences = MyApplication.instance.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", token)
                .build()
        } else {
            originalRequest
        }
        chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
