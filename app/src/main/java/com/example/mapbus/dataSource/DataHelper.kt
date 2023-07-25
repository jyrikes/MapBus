package com.example.mapbus.dataSource



import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

    class DataHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,
        DATABASE_VERSION) {
        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.execSQL(CREATE_TABLE_ROTA)
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        }
        companion object{
            private const val  DATABASE_NAME = "rotas.db"
            private const val  DATABASE_VERSION = 1
            private const val  CREATE_TABLE_ROTA = "CREATE TABLE ${DataDefinitions.Rota.TABLE_NAME}(" +
                    "${DataDefinitions.Rota.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DataDefinitions.Rota.Columns.NOME_DA_ROTA} TEXT," +
                    "${DataDefinitions.Rota.Columns.PARADA} TEXT," +
                    "${DataDefinitions.Rota.Columns.HORARIO} TEXT);"
        }

    }


