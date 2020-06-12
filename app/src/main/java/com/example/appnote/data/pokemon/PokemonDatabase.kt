package com.example.appnote.data.pokemon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appnote.model.PokemonRoom

@Database(entities = [PokemonRoom::class], version = 1, exportSchema = false)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object{
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PokemonDatabase::class.java,
                    "pokemon.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}