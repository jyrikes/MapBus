package com.example.mapbus.model

data class Rota (
            var id :Int,
            var nome_ponto: String?,
            var horario: String
        ){
    constructor(horario: String,parada: String,) : this(0,horario,parada)
}