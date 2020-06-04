package com.example.appnote.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {


    private fun client() = OkHttpClient.Builder().addInterceptor {chain ->
        val request = chain.request().newBuilder()
            .build()
        chain.proceed(request)
    }.build()

    fun retrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .client(client())
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}