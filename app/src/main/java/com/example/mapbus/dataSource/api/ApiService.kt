package com.example.mapbus.dataSource.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
         @POST("/user")
    fun enviarLocalizacao(@Body localizacao: Localizacao): Call<Localizacao>
    @GET("/user")
    fun obterLocalizacao(): Call<Localizacao>
}
