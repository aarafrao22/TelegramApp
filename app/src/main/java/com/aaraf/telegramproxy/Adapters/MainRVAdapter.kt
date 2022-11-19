package com.aaraf.telegramproxy.Adapters

import android.content.Context
import android.content.Intent
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

class MainRVAdapter(private val context: Context, val item: List<Proxy>) :
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

        var txtCountry: TextView = itemView.findViewById<TextView>(R.id.txtCountry)
        var imgIcon: ImageView = itemView.findViewById<ImageView>(R.id.imgFlag)
        var txtIP: TextView = itemView.findViewById<TextView>(R.id.txtIP)
        var txtLoad: TextView = itemView.findViewById<TextView>(R.id.txtSpeed)
        var txtType: TextView = itemView.findViewById<TextView>(R.id.txtType)


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
            Glide.with(itemView.context).load(icon).into(imgIcon);

            itemView.findViewById<LinearLayout>(R.id.btnCTA).setOnClickListener {
                Toast.makeText(itemView.context, "OpenAnotherApp here ${url}", Toast.LENGTH_SHORT)
                    .show()
            }
//            itemView.setOnClickListener {
//
//                val setIntent = Intent(itemView.context, setActivity::class.java)
//                setIntent.putExtra("Title", country)
//                setIntent.putExtra("Sets", txtIP)
//                itemView.context.startActivity(setIntent)
//            }
        }


    }
}