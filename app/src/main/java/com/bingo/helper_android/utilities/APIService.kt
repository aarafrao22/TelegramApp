package com.bingo.helper_android.utilities

import com.bingo.helper_android.models.BaseURLModel
import com.bingo.helper_android.models.GetProxyList
import com.bingo.helper_android.models.MoreAppsModel
import com.bingo.helper_android.models.NotificationModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("proxy")
    fun getProxiesList(@Query("token") token: String): Call<GetProxyList>

    @GET("notices")
    fun getNotification(): Call<NotificationModel>

    @GET("apps")
    fun getMoreApps(): Call<MoreAppsModel>


    @GET("v1/url?ts=9999")
    fun getBaseURL(): Call<BaseURLModel>

}