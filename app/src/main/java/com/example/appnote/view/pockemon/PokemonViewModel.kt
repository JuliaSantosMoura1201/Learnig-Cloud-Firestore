package com.example.appnote.view.pockemon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.pokemon.PokemonRepository

class PokemonViewModel(val app: Application): AndroidViewModel(app) {

    private val repository: PokemonRepository = PokemonRepository(app)
    val pokemonData = repository.liveDataResponse
}