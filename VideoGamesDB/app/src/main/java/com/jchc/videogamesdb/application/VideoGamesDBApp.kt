package com.jchc.videogamesdb.application

import android.app.Application
import com.jchc.videogamesdb.data.GameRepository
import com.jchc.videogamesdb.data.db.GameDatabase

class VideoGamesDBApp:Application() {

    private val database by lazy{
        GameDatabase.getDatabase(this@VideoGamesDBApp)
    }

    val repository by lazy{
        GameRepository(database.gameDao())
    }
}