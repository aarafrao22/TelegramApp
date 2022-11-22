package com.bingo.helper_android.adapters

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
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
import com.bingo.helper_android.models.AppX
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback


class AppsAdapter(private val context: Context, private val item: List<AppX>) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>(), OnUserEarnedRewardListener {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.item_more_apps, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(
            item[position].title,
            item[position].detail,
            item[position].ver,
            item[position].logo,
            item[position].download,
            item[position].platform,
        )
    }

    override fun getItemCount(): Int {
        return item.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        private var imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        private var imgPlatform: ImageView = itemView.findViewById(R.id.imgPlatform)
        private var txtVersion: TextView = itemView.findViewById(R.id.txtVersion)
        private var txtDetail: TextView = itemView.findViewById(R.id.txtDetail)


        fun setData(
            txtTitle: String?,
            txtDetail: String?,
            ver: String,
            imgLogo: String,
            downloadUrl: String,
            platform: String

        ) {
            this.txtTitle.text = txtTitle
            this.txtVersion.text = ver
            this.txtDetail.text = txtDetail

            if (platform == "android") {
                imgPlatform.setImageResource(R.drawable.android_line)
            } else {
                imgPlatform.setImageResource(R.drawable.windows_line)
            }
            Glide.with(itemView.context).load(imgLogo).into(imgIcon)

            itemView.findViewById<LinearLayout>(R.id.btnDownload).setOnClickListener {
                try {
                    MobileAds.initialize(context) {
                        loadAd()
                    }
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(downloadUrl)
                    itemView.context.startActivity(i)
                } catch (na: Exception) {
                    Toast.makeText(itemView.context, na.toString(), Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    private fun loadAd() {
        RewardedInterstitialAd.load(context,
            "ca-app-pub-9016979133569630/2871565216",
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                    rewardedInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                Log.d(TAG, "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad dismissed fullscreen content.")
                                rewardedInterstitialAd = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.")
                                rewardedInterstitialAd = null
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

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    rewardedInterstitialAd = null
                }
            })
        rewardedInterstitialAd?.show(/* Activity */ context as Activity, /*
    OnUserEarnedRewardListener */ this
        )
    }

    override fun onUserEarnedReward(p0: RewardItem) {


    }

}