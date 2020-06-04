package com.example.appnote.view.pockemon

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnote.R
import kotlinx.android.synthetic.main.activity_pokemon.*

class PokemonActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configSharedPreferences()
        setContentView(R.layout.activity_pokemon)

        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)

        rvPokemon.layoutManager = LinearLayoutManager(
            this@PokemonActivity,
            RecyclerView.HORIZONTAL,
            false
        )
        viewModel.getPokemon().observe(this, Observer {
            if (it != null){
                val list: ArrayList<String> = arrayListOf()
                list.add(it[1].name)
                list.add(it[2].name)
                list.add(it[3].name)
                list.add(it[4].name)
                list.add(it[5].name)

                rvPokemon.adapter = PokemonAdapter(list)
            }

        })
    }

    private fun configSharedPreferences(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        val themeKey = "currentTheme"

        when(sharedPreferences.getString(themeKey, "pattern")){
            "optionYellow" -> theme.applyStyle(R.style.OverlayThemeYellow, true)
            "optionPurple" -> theme.applyStyle(R.style.OverlayThemePurple, true)
            "pattern" -> theme.applyStyle(R.style.OverlayThemeLine, true)
        }
    }
}
