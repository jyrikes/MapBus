package com.example.mapbus.dataSource.api

interface LocalizacaoCallback {
    fun onLocalizacaoRecebida(localizacao: Localizacao)
    fun onFalhaNaRequisicao(erro: String)
}
