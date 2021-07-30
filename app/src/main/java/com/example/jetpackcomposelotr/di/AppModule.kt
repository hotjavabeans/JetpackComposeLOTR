package com.example.jetpackcomposelotr.di

import com.example.jetpackcomposelotr.data.remote.LOTRApi
import com.example.jetpackcomposelotr.data.remote.TokenInterceptor
import com.example.jetpackcomposelotr.repository.LOTRRepository
import com.example.jetpackcomposelotr.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLOTRRepository(
        api: LOTRApi
    ) = LOTRRepository(api)

    /*val interceptor = TokenInterceptor()
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()*/

    @Singleton
    @Provides
    fun provideLOTRApi(): LOTRApi {
        return Retrofit.Builder()
            /*.client(client)*/
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LOTRApi::class.java)
    }
}