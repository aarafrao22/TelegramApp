package com.aaraf.telegramproxy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aaraf.telegramproxy.R
import com.aaraf.telegramproxy.models.App
import com.aaraf.telegramproxy.models.AppX
import com.bumptech.glide.Glide


class AppsAdapter(private val item: List<AppX>) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

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
                Toast.makeText(itemView.context, downloadUrl, Toast.LENGTH_SHORT).show()
            }


        }
    }
}