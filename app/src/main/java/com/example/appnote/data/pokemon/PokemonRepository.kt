package com.example.appnote.data.pokemon

import com.example.appnote.model.Pokemon
import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.ListPokemon
import com.example.appnote.utils.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository {

    private val client = Client.retrofitInstance().create(PokemonService::class.java)

    fun getPokemon(): MutableLiveData<List<Pokemon>>{
        val liveDataResponse:MutableLiveData<List<Pokemon>> = MutableLiveData()

        client.getAll().enqueue(object : Callback<ListPokemon> {
            override fun onFailure(call: Call<ListPokemon>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ListPokemon>, response: Response<ListPokemon>) {
                var list : List<Pokemon> = listOf()
                if(response.isSuccessful){
                   response.body()?.let {
                       list = it.results
                   }
                }

                liveDataResponse.postValue(list)
            }

        })

        return  liveDataResponse
    }
}