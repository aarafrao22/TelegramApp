package com.aaraf.telegramproxy

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaraf.telegramproxy.adapters.AppsAdapter
import com.aaraf.telegramproxy.adapters.MainRVAdapter
import com.aaraf.telegramproxy.models.*
import com.aaraf.telegramproxy.utilities.APIService
import com.aaraf.telegramproxy.utilities.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class MoreAppsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var rv: RecyclerView
    private lateinit var imgBack: ImageView
    private lateinit var rvAdapter: AppsAdapter
    var count = 0
    lateinit var arrayList: MutableList<AppX>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_apps)

        getMoreApps()
        arrayList = mutableListOf()
        recyclerView()

    }

    private fun recyclerView() {
        rv = findViewById(R.id.moreAppsRV)
        imgBack = findViewById(R.id.imgBack)
        imgBack.setOnClickListener(this)
        val layout: RecyclerView.LayoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv.layoutManager = layout
        rvAdapter = AppsAdapter(arrayList)
        rv.adapter = rvAdapter
    }

    private fun getMoreApps() {
        val response = ServiceBuilder.getInstance(APIService::class.java)
        response.getMoreApps().enqueue(object : Callback<MoreAppsModel> {
            override fun onResponse(
                call: Call<MoreAppsModel>, response: Response<MoreAppsModel>
            ) {
                val receivedObj: MoreAppsModel = response.body()!!
                for (p in receivedObj.apps) {
                    arrayList.add(p)
                }
                rvAdapter.notifyDataSetChanged()
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

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                onBackPressed()
            }
        }
    }

}