package com.bingo.helper_android

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.bingo.helper_android.adapters.MainRVAdapter
import com.bingo.helper_android.models.*
import com.bingo.helper_android.utilities.APIService
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
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
    lateinit var adView: AdView

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"
    private lateinit var imgMsg: ImageView
    private lateinit var rvAdapter: MainRVAdapter
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null

    lateinit var arrayList: MutableList<Proxy>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getIntentData()
        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)



        drawerLayout = findViewById(R.id.my_drawer_layout)
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

        arrayList = mutableListOf()
        recyclerView()

        getProxyList(token)
        getNotification()

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
        rvAdapter = MainRVAdapter(arrayList)
        rv.adapter = rvAdapter
    }

    private fun getProxyList(s: String) {
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
                var intent = Intent(this@MainActivity, MoreAppsActivity::class.java)
                intent.putExtra("url", BASE_URL)
                startActivity(intent)
            }
        }
    }

    override fun onUserEarnedReward(p0: RewardItem) {

    }
}