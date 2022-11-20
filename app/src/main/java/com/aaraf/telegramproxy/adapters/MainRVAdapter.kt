package com.aaraf.telegramproxy.adapters

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aaraf.telegramproxy.R
import com.aaraf.telegramproxy.models.Proxy
import com.bumptech.glide.Glide


class MainRVAdapter(private val item: List<Proxy>) :
    RecyclerView.Adapter<MainRVAdapter.ViewHolder>() {

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
        private var txtIP: TextView = itemView.findViewById(R.id.txtIP)
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
            this.txtCountry.text = country
            this.txtIP.text = txtIP
            this.txtType.text = txtType
            this.txtCountry.text = country
            this.txtLoad.text = txtLoad
            Glide.with(itemView.context).load(icon).into(imgIcon)

            itemView.findViewById<LinearLayout>(R.id.btnCTA).setOnClickListener {

                try {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setPackage("org.telegram.messenger");
                    i.data = Uri.parse(url)
                    itemView.context.startActivity(i)
                } catch (na: PackageManager.NameNotFoundException) {
                    Toast.makeText(itemView.context, "App not Installed", Toast.LENGTH_SHORT).show()
                }

            }


        }
    }
}