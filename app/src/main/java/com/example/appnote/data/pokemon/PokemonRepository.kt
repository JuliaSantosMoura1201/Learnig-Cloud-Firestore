package com.example.appnote.data.pokemon


import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.PokemonRoom
import com.example.appnote.utils.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PokemonRepository(val app: Application) {

    private val pokemonDao = PokemonDatabase.getDatabase(app).pokemonDao()
    val liveDataResponse = MutableLiveData<ArrayList<String>>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data = pokemonDao.getAll()
            val dataList = arrayListOf<String>()
            for (pokemon in data){
                dataList.add(pokemon.name)
            }
            if(data.isEmpty()){
                getData()
            }else{
                liveDataResponse.postValue(dataList)
            }
        }
    }

    private fun getData(){
        val client = Client.retrofitInstance()

        CoroutineScope(Dispatchers.IO).launch {
            val response = client.getAll()

                try {
                    if (response.isSuccessful){

                        val list = response.body()?.results
                        val names: ArrayList<String> = arrayListOf()

                        if(list != null){

                            val listRoom = ArrayList<PokemonRoom>()
                            for(pokemon in list){
                                names.add(pokemon.name)
                                listRoom.add(PokemonRoom(0, pokemon.name))
                            }

                            liveDataResponse.postValue(names)
                            pokemonDao.deleteAll()
                            pokemonDao.insertAllPokemon(listRoom)
                        }

                    }
                }catch (e: HttpException){

                }
        }
    }


}