package com.example.mapbus.dataSource.api

import com.example.mapbus.model.Rota

interface RotalCallback {

    fun onRotaRecebida(rota: Rota)
    fun onFalhaNaRequisicao(erro: String)
}