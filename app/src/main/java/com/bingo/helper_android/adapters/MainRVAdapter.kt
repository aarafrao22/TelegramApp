package com.bingo.helper_android.adapters

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bingo.helper_android.R
import com.bingo.helper_android.models.Proxy
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


@Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")
class MainRVAdapter(private val activity: Activity, private val item: List<Proxy>) :
    RecyclerView.Adapter<MainRVAdapter.ViewHolder>() {
    private var mRewardedAd: RewardedAd? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.item_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(
            item[position].country,
            item[position].icon,
            item[position].text,
            item[position].type,
            item[position].load,
            item[position].url,
        )
    }

    override fun getItemCount(): Int {
        return item.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var txtCountry: TextView = itemView.findViewById(R.id.txtCountry)
        private var imgIcon: ImageView = itemView.findViewById(R.id.imgFlag)
        private var imgSpeed: ImageView = itemView.findViewById(R.id.imgSpeed)
        private var txtSecret: TextView = itemView.findViewById(R.id.txtSecret)
        private var txtIP: TextView = itemView.findViewById(R.id.txtIP)
        private var txtPort: TextView = itemView.findViewById(R.id.txtPort)
        private var txtLoad: TextView = itemView.findViewById(R.id.txtSpeed)
        private var txtType: TextView = itemView.findViewById(R.id.txtType)


        fun setData(
            country: String?,
            icon: String?,
            txtIP: String,
            txtType: String,
            txtLoad: String,
            url: String
        ) {

            val raw = url.split("&");
            Log.d(TAG, "setData: $raw")
            this.txtCountry.text = country
            this.txtSecret.text = txtIP
            this.txtType.text = txtType
            this.txtCountry.text = country
            this.txtLoad.text = txtLoad
            this.txtPort.text = raw[1]
            this.txtIP.text = raw[0].split("server=").get(1)


            if (txtLoad.toInt() <= 30) {
                //red
                imgSpeed.setImageResource(R.drawable.red_ic_round_signal_cellular_alt)
            } else if (txtLoad.toInt() <= 70) {
                //yellow
                imgSpeed.setImageResource(R.drawable.yellow_ic_round_signal_cellular_alt)
            } else {

                imgSpeed.setImageResource(R.drawable.ic_round_signal_cellular_alt)
            }
            Glide.with(itemView.context).load(icon).into(imgIcon)

            itemView.findViewById<LinearLayout>(R.id.btnCTA).setOnClickListener {
                loadAd(itemView)
                try {
                    if (mRewardedAd != null) {
                        mRewardedAd!!.show(activity, OnUserEarnedRewardListener {
                            val i = Intent(Intent.ACTION_VIEW)
                            i.setPackage("org.telegram.messenger")
                            i.data = Uri.parse(url)
                            itemView.context.startActivity(i)
                        })

                    } else {

                        val i = Intent(Intent.ACTION_VIEW)
                        i.setPackage("org.telegram.messenger")
                        i.data = Uri.parse(url)
                        itemView.context.startActivity(i)

                    }

                } catch (na: PackageManager.NameNotFoundException) {
                    Toast.makeText(itemView.context, "App not Installed", Toast.LENGTH_SHORT).show()
                }

            }


        }
    }

    private fun loadAd(itemView: View) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            activity,
//            itemView.context.getString(R.string.Reward_Unit_ID),
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mRewardedAd = rewardedAd
                }
            })
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }
}