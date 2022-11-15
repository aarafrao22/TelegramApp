package com.aaraf.telegramproxy.models

data class GetAppList(
    val apps: List<App>,
    val code: Int
)