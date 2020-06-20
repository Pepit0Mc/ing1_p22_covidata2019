package com.example.covidata

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

const val COUNTRY = "com.example.covidata.COUNTRY"

class CountryAdapter(val context: Activity, val data: List<Country>) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val countryTextView: TextView = itemview.findViewById(R.id.list_country_item_country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false)

        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country : Country = data[position]
        holder.countryTextView.text = country.Country
        holder.countryTextView.setOnClickListener {
            val intent = Intent(context, CountryData::class.java).apply {
                putExtra(COUNTRY, country.Slug)
            }
            context.startActivity(intent)
        }
    }
}