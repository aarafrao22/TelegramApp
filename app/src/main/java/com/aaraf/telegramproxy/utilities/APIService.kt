package com.aaraf.telegramproxy.utilities

import com.aaraf.telegramproxy.models.GetProxyList
import com.aaraf.telegramproxy.models.MoreAppsModel
import com.aaraf.telegramproxy.models.NotificationModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("v1/proxy")
    fun getProxiesList(@Query("token") token: String): Call<GetProxyList>

    @GET("v1/notices")
    fun getNotification(): Call<NotificationModel>

    @GET("v1/apps")
    fun getMoreApps(): Call<MoreAppsModel>

}