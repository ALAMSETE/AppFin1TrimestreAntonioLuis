package com.example.appfin1trimestreantonioluis

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val base_user = "http://iesayala.ddns.net/alamsete/"

interface CocheInterface {
    // Aqui ponemos el nombre del archivo php que se encargará de recoger el JSON
    @GET("generarjson.php")
    fun cocheInformation(): Call<CocheInfo>
}

object CocheInstance {
    val cocheInterface: CocheInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_user)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cocheInterface = retrofit.create(CocheInterface::class.java)
    }


}