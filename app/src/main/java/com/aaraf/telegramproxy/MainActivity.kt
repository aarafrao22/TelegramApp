package com.aaraf.telegramproxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aaraf.telegramproxy.models.GetProxyList
import com.aaraf.telegramproxy.utilities.APIService
import com.aaraf.telegramproxy.utilities.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var token = "testtoken"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getProxyList(token)

    }

    private fun getProxyList(s: String) {
        val response = ServiceBuilder.getInstance(APIService::class.java)
        response.getProxiesList(s).enqueue(
            object : Callback<GetProxyList> {
                override fun onResponse(
                    call: Call<GetProxyList>,
                    response: Response<GetProxyList>
                ) {
                    Toast.makeText(this@MainActivity, response.body().toString(), Toast.LENGTH_LONG)
                        .show()
                }

                override fun onFailure(call: Call<GetProxyList>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
                }

            }
        )
    }
}