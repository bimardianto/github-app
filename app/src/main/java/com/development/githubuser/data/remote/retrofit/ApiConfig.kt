package com.development.githubuser.data.remote.retrofit

import com.development.githubuser.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiConfig {

    private const val authToken = BuildConfig.API_KEY
    private const val authURL = BuildConfig.BASE_URL
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor(authToken)).build()

    private val retrofit =
        Retrofit
            .Builder()
            .baseUrl(authURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val apiService = retrofit.create<ApiService>()

}