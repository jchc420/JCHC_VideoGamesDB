package com.jchc.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jchc.videogamesdb.util.Constants

@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class GameEntity(
    @PrimaryKey(autoGenerate = true) //Para que lo haga llave primaria y se autogenere
    @ColumnInfo(name="game_id")
    val id: Long = 0,
    @ColumnInfo(name="game_title")
    var title: String,
    @ColumnInfo(name="game_genre")
    var genre: String,
    @ColumnInfo(name="game_developer", defaultValue = "Desconocido")
    var developer: String
)