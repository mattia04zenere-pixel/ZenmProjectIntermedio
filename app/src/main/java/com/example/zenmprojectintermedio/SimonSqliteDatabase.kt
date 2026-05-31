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

    // creazioen fisica delle colenne del database
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

    // funzione per inserire la partita nel database
    fun insertGame(sequence: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SEQUENCE, sequence)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // funzione per ottenere tutte le partite dal database, legendo quelle salvate
    fun getAllGames(): List<String> {
        val list = mutableListOf<String>()
        val db = this.readableDatabase
        // query per prendere le partite salvate, in linguaggio sql
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