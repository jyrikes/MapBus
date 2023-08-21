package com.example.mapbus.dataSource.api

data class Localizacao(
    val horario: String?,
    val latitude: Double,
    val longitude: Double,
    val rota_id: Int?
)