package com.example.appnote.view.pockemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appnote.R
import kotlinx.android.synthetic.main.pokemon.view.*

class PokemonAdapter (private val pokemons: ArrayList<String>)

    : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       holder.itemView.txtPokemonName.text = pokemons[position+1]

        when(position){
            0 -> holder.itemView.ivPokemon.setImageResource(R.drawable.wobbuffet)
            1 -> holder.itemView.ivPokemon.setImageResource(R.drawable.girafarig)
            2 -> holder.itemView.ivPokemon.setImageResource(R.drawable.pineco)
            3 -> holder.itemView.ivPokemon.setImageResource(R.drawable.forretress)
            4 -> holder.itemView.ivPokemon.setImageResource(R.drawable.dunsparce)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}