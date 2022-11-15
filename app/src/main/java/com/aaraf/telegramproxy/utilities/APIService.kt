package com.aaraf.telegramproxy.utilities

import com.aaraf.telegramproxy.models.GetProxyList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("v1/proxy")
    fun getProxiesList(@Query("token") token: String): Call<GetProxyList>

}