package com.example.mapbus.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.mapbus.dataSource.DataDefinitions
import com.example.mapbus.dataSource.DataHelper
import com.example.mapbus.model.Rota

class RotaRepository(context: Context) {
    private val dbHelper = DataHelper(context)

    fun save(rota : Rota):Int{
        //colocar em modo escrita
        val db = dbHelper.writableDatabase
        //criar o mapa
        val valores = ContentValues()
        valores.put(DataDefinitions.Rota.Columns.NOME_DA_ROTA ,rota.nomeRota)
        valores.put(DataDefinitions.Rota.Columns.PARADA ,rota.parada)
        valores.put(DataDefinitions.Rota.Columns.HORARIO ,rota.horario)
        //inserindo os valores
        val id = db.insert(DataDefinitions.Rota.TABLE_NAME, null ,valores)
        return  id.toInt()
    }
    @SuppressLint("Range")
    fun getJogos():ArrayList<Rota>{
        //colocar o banco de dados em modo leitura
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DataDefinitions.Rota.TABLE_NAME,null,null,null,null,null,null,null

        )
        var jogos = ArrayList<Rota>()
        if(cursor!=null){
            while(cursor.moveToNext()){
                var rota = Rota(
                    cursor.getInt(cursor.getColumnIndex(DataDefinitions.Rota.Columns.ID)),
                    cursor.getString(cursor.getColumnIndex(DataDefinitions.Rota.Columns.NOME_DA_ROTA)),
                    cursor.getString(cursor.getColumnIndex(DataDefinitions.Rota.Columns.PARADA)),
                    cursor.getString(cursor.getColumnIndex(DataDefinitions.Rota.Columns.HORARIO))
                )
                jogos.add(rota)
            }
        }
        return jogos
    }


}