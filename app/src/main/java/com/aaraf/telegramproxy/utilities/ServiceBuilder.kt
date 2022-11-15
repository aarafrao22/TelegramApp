package com.aaraf.telegramproxy.utilities

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    val BASEURL = "https://ir.muzhifei.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASEURL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> getInstance(service: Class<T>): T {
        return retrofit.create(service)
    }
}