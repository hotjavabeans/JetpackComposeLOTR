package com.example.jetpackcomposelotr.data.remote

import com.example.jetpackcomposelotr.util.Constants
import com.example.jetpackcomposelotr.util.Constants.ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", ACCESS_TOKEN)
            .build()

        return chain.proceed(newRequest)
    }
}