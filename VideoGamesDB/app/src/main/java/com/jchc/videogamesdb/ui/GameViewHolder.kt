package com.jchc.videogamesdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.jchc.videogamesdb.data.db.model.GameEntity
import com.jchc.videogamesdb.databinding.GameElementBinding

class GameViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root) {

    val ivIcon = binding.ivIcon

    fun bind(game: GameEntity){
        binding.apply {
            tvTitle.text = game.title
            tvGenre.text = game.genre
            tvDeveloper.text = game.developer
        }
    }
}