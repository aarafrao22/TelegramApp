package com.aaraf.telegramproxy.models

data class Proxy(
    val `class`: Int,
    val country: String,
    val icon: String,
    val id: String,
    val level: Int,
    val load: String,
    val type: String,
    val url: String,
    val text: String
)