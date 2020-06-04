package com.example.appnote.view.pockemon

import com.example.appnote.model.Pokemon
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.pokemon.PokemonRepository

class PokemonViewModel: ViewModel() {

    val repository: PokemonRepository = PokemonRepository()

    fun getPokemon(): LiveData<List<Pokemon>>{
        return repository.getPokemon()
    }
}