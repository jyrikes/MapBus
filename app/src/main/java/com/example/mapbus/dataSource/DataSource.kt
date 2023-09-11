package com.example.mapbus.dataSource

import com.example.mapbus.model.Rota

class DataSource {
    companion object {
        private val listaRotas: MutableList<Rota> = mutableListOf()

        fun getRota(): MutableList<Rota> {
            return listaRotas
        }

        fun adicionarRota(rota: Rota) {
            listaRotas.add(0,rota)
        }
    }
}
