package com.example.appnote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonTable")
data class PokemonRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)