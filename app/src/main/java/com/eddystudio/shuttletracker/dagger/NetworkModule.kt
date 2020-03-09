package com.studio.eddy.myapplication.dagger

import com.studio.eddy.myapplication.network.ShuttleService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): ShuttleService {
        return Retrofit.Builder()
            .baseUrl("https://ucsdbus.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShuttleService::class.java)
    }
}