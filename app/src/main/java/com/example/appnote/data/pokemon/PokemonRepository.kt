package com.example.appnote.data.pokemon


import androidx.lifecycle.MutableLiveData
import com.example.appnote.utils.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PokemonRepository {

    fun getData(): MutableLiveData<ArrayList<String>>{
        val client = Client.retrofitInstance()
        val liveDataResponse = MutableLiveData<ArrayList<String>>()

        CoroutineScope(Dispatchers.IO).launch {
            val response = client.getAll()

            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful){
                        val list = response.body()?.results
                        val names: ArrayList<String> = arrayListOf()
                        if(list != null){
                            for(pokemon in list){
                                names.add(pokemon.name)
                            }
                            liveDataResponse.postValue(names)
                        }

                    }
                }catch (e: HttpException){

                }
            }
        }

        return liveDataResponse
    }


}