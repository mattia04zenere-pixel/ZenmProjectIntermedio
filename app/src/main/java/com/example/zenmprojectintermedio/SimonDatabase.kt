package com.example.zenmprojectintermedio

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

// La tabella del database che conterrà le stringhe delle partite
@Entity(tableName = "game_history")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sequence: String
)

// Le operazioni di lettura e scrittura
@Dao
interface GameDao {
    @Query("SELECT sequence FROM game_history ORDER BY id DESC")
    fun getAllGames(): List<String>

    @Insert
    fun insertGame(game: GameResult)
}

// Il gestore del file del database sul telefono
@Database(entities = [GameResult::class], version = 1, exportSchema = false)
abstract class SimonRoomDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: SimonRoomDatabase? = null

        fun getDatabase(context: Context): SimonRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SimonRoomDatabase::class.java,
                    "simon_database"
                ).allowMainThreadQueries() // Semplifica la gestione dei thread per l'esame
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}