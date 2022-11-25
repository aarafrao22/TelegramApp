package com.bingo.helper_android

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bingo.helper_android.adapters.MainRVAdapter
import com.bingo.helper_android.models.*
import com.bingo.helper_android.utilities.APIService
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, OnUserEarnedRewardListener {
    private var BASE_URL = ""
    private var token = "testtoken"
    private var drawerLayout: DrawerLayout? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var rv: RecyclerView
    private lateinit var mAdView: AdView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var imgMsg: ImageView
    private lateinit var rvAdapter: MainRVAdapter

    lateinit var arrayList: MutableList<Proxy>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getIntentData()
        getBannerAd()

        drawerLayout = findViewById(R.id.my_drawer_layout)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.nav_open, R.string.nav_close
        )

        drawerLayout!!.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        setNavigationViewListener()

        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val drawable = AppCompatResources.getDrawable(applicationContext, R.drawable.bg_wh)
        supportActionBar!!.setBackgroundDrawable(drawable)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.vector)

        swipeRefreshLayout.setOnRefreshListener {
            Log.d(TAG, "swiped")
            getURL()
        }

        arrayList = mutableListOf()
        recyclerView()

        getProxyList(token)
        getNotification()

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
                    swipeRefreshLayout.isRefreshing = false
                    if (response.body() != null) {
                        val receivedObj: BaseURLModel = response.body()!!
                        BASE_URL = receivedObj.url + "/"
                        getProxyList(token)

                    }else{
                        Toast.makeText(this@MainActivity, "Server Not responding", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<BaseURLModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()

                }

            })
    }

    private fun getBannerAd() {
        val adView = AdView(this)
        adView.adUnitId = getString(R.string.Banner_Unit_ID)
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                Log.d(TAG, "onAdImpression: Code to be executed when an impression is recorded")
            // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

    }


    private fun getIntentData() {
        BASE_URL = intent.getStringExtra("url").toString()
    }

    private fun setNavigationViewListener() {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun recyclerView() {
        rv = findViewById(R.id.mainRV)
        imgMsg = findViewById(R.id.imgMsg)
        imgMsg.setOnClickListener(this)
        val layout: LayoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv.layoutManager = layout
        rvAdapter = MainRVAdapter(this@MainActivity, arrayList)
        rv.adapter = rvAdapter
    }

    private fun getProxyList(s: String) {
        if (BASE_URL != "") {

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitAPI: APIService = retrofit.create(APIService::class.java)
            val call: Call<GetProxyList> = retrofitAPI.getProxiesList(s)

            call.enqueue(object : Callback<GetProxyList> {
                override fun onResponse(
                    call: Call<GetProxyList>, response: Response<GetProxyList>
                ) {
                    if (response.body() != null) {
                        arrayList = mutableListOf()
                        val receivedObj: ProxyList = response.body()!!.proxylist
                        Log.d(TAG, "swipeResponse: $receivedObj")
                        for (p in receivedObj.proxy) {
                            arrayList.add(p)
                        }
                        rvAdapter.notifyDataSetChanged()
                        recyclerView()
                    }

                }

                override fun onFailure(call: Call<GetProxyList>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
                }

            })
        }

    }

    private fun getNotification() {
        if (BASE_URL != "") {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retrofitAPI: APIService = retrofit.create(APIService::class.java)
            val call: Call<NotificationModel> = retrofitAPI.getNotification()

            call.enqueue(object : Callback<NotificationModel> {
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.share_up -> {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                    var shareMessage = "\nLet me recommend you this application\n\n"
                    shareMessage = """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}


                """.trimIndent()
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch (e: Exception) {
                    //e.toString();
                }
            }
            R.id.about_us -> {
                showAboutUsDialogue()
            }
            R.id.rate_us -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                }
            }
        }
        //close navigation drawer
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showAboutUsDialogue() {
        Toast.makeText(applicationContext, "showAboutUsDialogue", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgMsg -> {
                val intent = Intent(this@MainActivity, MoreAppsActivity::class.java)
                intent.putExtra("url", BASE_URL)
                startActivity(intent)
            }
        }
    }

    override fun onUserEarnedReward(p0: RewardItem) {

    }
}