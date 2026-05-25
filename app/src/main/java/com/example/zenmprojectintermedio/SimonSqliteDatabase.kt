package com.example.zenmprojectintermedio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SimonSqliteDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //creazione variabili per il database
    companion object {
        private const val DATABASE_NAME = "simon_pure_database.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "game_history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SEQUENCE = "sequence"
    }

    // Qui creiamo fisicamente la tabella con una query SQL pura
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_SEQUENCE TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Metodo per inserire una partita nel database SQL
    fun insertGame(sequence: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SEQUENCE, sequence)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Metodo per leggere tutte le partite dal database SQL
    fun getAllGames(): List<String> {
        val list = mutableListOf<String>()
        val db = this.readableDatabase
        // Query SQL per prendere i dati ordinati dall'ultimo inserito
        val cursor = db.rawQuery("SELECT $COLUMN_SEQUENCE FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}