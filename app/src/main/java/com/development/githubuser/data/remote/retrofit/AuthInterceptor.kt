package com.development.githubuser.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder().addHeader("Authorization", authToken).build()
        return chain.proceed(modifiedRequest)
    }
}