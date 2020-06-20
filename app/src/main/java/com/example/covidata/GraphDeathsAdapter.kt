package com.example.covidata

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GraphDeathsAdapter(val context: Activity, val data: List<CountryStat>) : RecyclerView.Adapter<GraphDeathsAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val dateTextView: TextView = itemview.findViewById(R.id.dateTextView)
        val valueTextView: TextView = itemview.findViewById(R.id.valueTextView)
        val progressBar: ProgressBar = itemview.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.graph_item, parent, false)

        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element : CountryStat = data[position]
        holder.dateTextView.text = element.Date.substringBefore('T')
        holder.valueTextView.text = element.Deaths.toString()
        holder.progressBar.setProgress((element.Deaths * 100) / data.last().Deaths)
    }
}