package com.example.mapbus.dataSource

import com.example.mapbus.model.Rota

class DataSource {
    companion object{
        fun getRota() : ArrayList<Rota> {
            val rotas = ArrayList<Rota>()
            rotas.add(Rota(100, "Rota 1", "07:25", "ENTRADA DA COHAB 1"))
            rotas.add(Rota(101, "Rota 2", "07:27", "PRAÇA DA CRIANÇA"))
            rotas.add(Rota(102, "Rota 3", "07:30", " SEBASTIÃO CABRAL"))
            rotas.add(Rota(103, "Rota 4", "07:35", "FÓRUM"))
            rotas.add(Rota(104, "Rota 5", "07:38", "ESCOLA PROF. DONINO"))
            rotas.add(Rota(105, "Rota 6", "07:40", "PLACA HOSPITAL SANTA FÉ"))
            rotas.add(Rota(106, "Rota 7", "07:43", "UABJ"))
            rotas.add(Rota(107, "Rota 8", "07:45", "AEB"))


            return rotas
        }

    }
}