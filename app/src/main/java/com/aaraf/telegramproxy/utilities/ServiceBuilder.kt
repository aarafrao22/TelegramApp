package com.aaraf.telegramproxy.utilities

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    val BASEURL = "https://ir.muzhifei.com/v1/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}