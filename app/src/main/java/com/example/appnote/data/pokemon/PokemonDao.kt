package com.example.appnote.data.pokemon

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appnote.model.PokemonRoom

@Dao
interface PokemonDao {

    @Query("SELECT * from pokemonTable")
    fun getAll(): List<PokemonRoom>

    @Insert
    suspend fun insertAllPokemon(pokemon: ArrayList<PokemonRoom>)

    @Query("DELETE from pokemonTable")
    suspend fun deleteAll()
}