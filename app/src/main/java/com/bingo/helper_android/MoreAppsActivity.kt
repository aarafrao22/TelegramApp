package com.bingo.helper_android

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bingo.helper_android.adapters.AppsAdapter
import com.bingo.helper_android.models.*
import com.bingo.helper_android.utilities.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("DEPRECATION")
class MoreAppsActivity : AppCompatActivity(), View.OnClickListener {
    private var BASE_URL: String = ""
    private lateinit var rv: RecyclerView
    private lateinit var imgBack: ImageView
    private lateinit var rvAdapter: AppsAdapter
    var count = 0
    lateinit var arrayList: MutableList<AppX>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_apps)

        getIntentData()

        getURL()
        arrayList = mutableListOf()
        recyclerView()

    }

    private fun getIntentData() {
        BASE_URL = intent.getStringExtra("url").toString()

    }

    private fun recyclerView() {
        rv = findViewById(R.id.moreAppsRV)
        imgBack = findViewById(R.id.imgBack)
        imgBack.setOnClickListener(this)
        val layout: RecyclerView.LayoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv.layoutManager = layout
        rvAdapter = AppsAdapter(this, arrayList)
        rv.adapter = rvAdapter
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
                if (response.body() != null) {
                    val receivedObj: BaseURLModel = response.body()!!
                    BASE_URL = receivedObj.url + "/"
                    getMoreApps()

                } else {
                    Toast.makeText(
                        this@MoreAppsActivity,
                        "Server Not responding",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

            override fun onFailure(call: Call<BaseURLModel>, t: Throwable) {
                Toast.makeText(this@MoreAppsActivity, t.toString(), Toast.LENGTH_LONG).show()

            }

        })
    }

    private fun getMoreApps() {

        if (BASE_URL != "") {

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitAPI: APIService = retrofit.create(APIService::class.java)
            val call: Call<MoreAppsModel> = retrofitAPI.getMoreApps()

            call.enqueue(object : Callback<MoreAppsModel> {
                override fun onResponse(
                    call: Call<MoreAppsModel>, response: Response<MoreAppsModel>
                ) {
                    if (response.body() != null) {
                        val receivedObj: MoreAppsModel = response.body()!!
                        for (p in receivedObj.apps) {
                            arrayList.add(p)
                        }
                        rvAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<MoreAppsModel>, t: Throwable) {
                    Toast.makeText(this@MoreAppsActivity, t.toString(), Toast.LENGTH_LONG).show()

                    //retry at least 3 times in case of any error
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

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                onBackPressed()
            }
        }
    }

}