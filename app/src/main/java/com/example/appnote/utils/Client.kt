package com.example.appnote.utils

import com.example.appnote.data.pokemon.PokemonService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Client {


    private fun client() = OkHttpClient.Builder().addInterceptor {chain ->
        val request = chain.request().newBuilder()
            .build()
        chain.proceed(request)
    }.build()

    fun retrofitInstance(): PokemonService {
        return Retrofit.Builder()
            .client(client())
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(PokemonService::class.java)

    }
}