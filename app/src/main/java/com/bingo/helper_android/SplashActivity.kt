package com.bingo.helper_android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bingo.helper_android.models.BaseURLModel
import com.bingo.helper_android.utilities.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    var count = 0
    var BASE_URL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        getURL()


    }

    private fun getURL() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.muzhifei.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI: APIService = retrofit.create(APIService::class.java)
        val call: Call<BaseURLModel> = retrofitAPI.getBaseURL()

        call.enqueue(object : Callback<BaseURLModel> {
            override fun onResponse(
                call: Call<BaseURLModel>, response: Response<BaseURLModel>
            ) {
                val receivedObj: BaseURLModel = response.body()!!

                BASE_URL = receivedObj.url + "/"


                val handler = Handler()
                handler.postDelayed({

                    val openMainActivity = Intent(this@SplashActivity, MainActivity::class.java)
                    openMainActivity.putExtra("url", BASE_URL)
                    startActivity(openMainActivity)
                    finish()

                }, 3000)

            }

            override fun onFailure(call: Call<BaseURLModel>, t: Throwable) {
                Toast.makeText(this@SplashActivity, t.toString(), Toast.LENGTH_LONG).show()

                if (count < 3) {
                    Handler().postDelayed({
                        getURL()
                    }, 1000)
                }
                count++
            }

        })
    }

}