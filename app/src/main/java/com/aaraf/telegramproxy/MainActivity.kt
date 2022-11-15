package com.aaraf.telegramproxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aaraf.telegramproxy.utilities.APIService
import com.aaraf.telegramproxy.utilities.ServiceBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val responseVar = ServiceBuilder.getInstance()
            .create(APIService::class.java)

        GlobalScope.launch {
            val response = responseVar.getProxiesList("testtoken")
            if (response != null)
                Log.d("RESPONSE", response.toString())
        }
    }
}