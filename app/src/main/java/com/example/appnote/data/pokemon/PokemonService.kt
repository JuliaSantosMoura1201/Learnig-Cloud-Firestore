package com.example.appnote.data.pokemon

import com.example.appnote.model.ListPokemon
import retrofit2.Response
import retrofit2.http.GET

interface PokemonService {

    @GET("pokemon?limit=100&offset=200")
    suspend fun getAll(): Response<ListPokemon>
}