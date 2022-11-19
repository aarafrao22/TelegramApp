package com.aaraf.telegramproxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.aaraf.telegramproxy.Adapters.MainRVAdapter
import com.aaraf.telegramproxy.models.GetProxyList
import com.aaraf.telegramproxy.models.NotificationModel
import com.aaraf.telegramproxy.models.Proxy
import com.aaraf.telegramproxy.models.ProxyList
import com.aaraf.telegramproxy.utilities.APIService
import com.aaraf.telegramproxy.utilities.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var token = "testtoken"

    private lateinit var rv: RecyclerView
    private lateinit var rvAdapter: MainRVAdapter

    lateinit var arrayList: MutableList<Proxy>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayList = mutableListOf()
        recyclerView()

        getProxyList(token)
        getNotification()

    }

    private fun recyclerView() {
        rv = findViewById(R.id.mainRV)
        val layout: LayoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv.layoutManager = layout
        rvAdapter = MainRVAdapter(applicationContext, arrayList)
        rv.adapter = rvAdapter
    }

    private fun getProxyList(s: String) {
        val response = ServiceBuilder.getInstance(APIService::class.java)
        response.getProxiesList(s).enqueue(object : Callback<GetProxyList> {
            override fun onResponse(
                call: Call<GetProxyList>, response: Response<GetProxyList>
            ) {

                Toast.makeText(this@MainActivity, response.body().toString(), Toast.LENGTH_LONG)
                    .show()
                val receivedObj: ProxyList = response.body()!!.proxylist
                for (p in receivedObj.proxy) {
                    arrayList.add(p)
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<GetProxyList>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getNotification() {
        val response = ServiceBuilder.getInstance(APIService::class.java)
        response.getNotification().enqueue(object : Callback<NotificationModel> {
            override fun onResponse(
                call: Call<NotificationModel>, response: Response<NotificationModel>
            ) {
                val received: NotificationModel = response.body()!!
                Toast.makeText(
                    this@MainActivity, received.notice, Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<NotificationModel>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }
}