package com.jchc.videogamesdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jchc.videogamesdb.data.db.model.GameEntity
import com.jchc.videogamesdb.util.Constants


@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = true //por defecto es true
)
abstract class GameDatabase: RoomDatabase() {
    //Aquí va el DAO
    abstract fun gameDao():GameDao

    companion object{
        @Volatile
        private var INSTANCE: GameDatabase? = null
        fun getDatabase(context: Context): GameDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}