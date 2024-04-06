package com.jchc.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jchc.videogamesdb.R
import com.jchc.videogamesdb.application.VideoGamesDBApp
import com.jchc.videogamesdb.data.GameRepository
import com.jchc.videogamesdb.data.db.model.GameEntity
import com.jchc.videogamesdb.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var games: List<GameEntity> = emptyList()
    private lateinit var repository: GameRepository

    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as VideoGamesDBApp).repository

        gameAdapter = GameAdapter() { selectedGame ->
            //Click para actualizar or borrar un elemento
            val dialog = GameDialog(
                newGame = false,
                game = selectedGame,
                {
                    updateUI()
                },{ action ->
                    message(action)
                })
            dialog.show(supportFragmentManager, "UpdateDialogo")
        }
        binding.rvGames.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
        }

        updateUI()
    }
    private fun updateUI(){
        lifecycleScope.launch(){
            games = repository.getAllGames()
            binding.tvSinRegistros.visibility =
                if(games.isEmpty()) View.VISIBLE else View.INVISIBLE

            gameAdapter.updateList(games)
        }
    }

    fun click(view: View) {
        //Manejo de click de FAB (Floating action button)
        val dialog = GameDialog( updateUI= {
            updateUI()
        },message = {

        })
        dialog.show(supportFragmentManager, "InsertDialogo")
    }

    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
}