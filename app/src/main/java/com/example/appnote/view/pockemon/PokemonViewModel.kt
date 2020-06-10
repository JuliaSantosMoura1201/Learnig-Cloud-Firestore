package com.example.appnote.view.pockemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.pokemon.PokemonRepository

class PokemonViewModel: ViewModel() {

    val repository: PokemonRepository = PokemonRepository()

    fun getPokemon(): LiveData<ArrayList<String>>{
        return repository.getData()
    }
}