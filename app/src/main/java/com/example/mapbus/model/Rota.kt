package com.example.mapbus.model

class Rota (
            var id :Int,
            var nomeRota :String,
            var parada: String,
            var horario: String
        ){
    constructor(nomeRota: String,horario: String,parada: String,) : this(0,nomeRota,horario,parada)
}