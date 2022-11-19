package com.aaraf.telegramproxy

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aaraf.telegramproxy.models.MoreAppsModel
import com.aaraf.telegramproxy.utilities.APIService
import com.aaraf.telegramproxy.utilities.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoreAppsActivity : AppCompatActivity() {

    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_apps)

        getMoreApps()


    }

    private fun getMoreApps() {
        val response = ServiceBuilder.getInstance(APIService::class.java)
        response.getMoreApps().enqueue(object : Callback<MoreAppsModel> {
            override fun onResponse(
                call: Call<MoreAppsModel>, response: Response<MoreAppsModel>
            ) {
                val received: MoreAppsModel = response.body()!!
                Toast.makeText(
                    this@MoreAppsActivity, received.apps.toString(), Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<MoreAppsModel>, t: Throwable) {
                Toast.makeText(this@MoreAppsActivity, t.toString(), Toast.LENGTH_LONG).show()

                if (count < 3) {
                    Handler().postDelayed({
                        getMoreApps()
                    }, 1000)
                }
                count++
            }

        })
    }

}