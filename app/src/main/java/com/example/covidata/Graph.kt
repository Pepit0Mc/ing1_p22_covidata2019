package com.example.covidata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_graph.*

class Graph : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val countryList = findViewById<RecyclerView>(R.id.CountryList);
        val countryListData : MutableList<String> = arrayListOf("France", "Spain", "England")
        countryList.adapter = CountryGraphAdapter(this@Graph, countryListData)
        countryList.setHasFixedSize(true)
        countryList.addItemDecoration(DividerItemDecoration(this@Graph, LinearLayoutManager.HORIZONTAL))
        countryList.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(countryList)

        countryList.addOnScrollListener(object:RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val element = snapHelper.findSnapView(recyclerView.layoutManager) ?: return
                val position = recyclerView.layoutManager?.getPosition(element)
            }
        })

        leftArrow.setOnClickListener {
            val element = snapHelper.findSnapView(countryList.layoutManager) ?: throw Exception("Element not found")
            val position = countryList.layoutManager?.getPosition(element) ?: throw Exception("Position not found")
            if (position != 0) {
                countryList.scrollToPosition(position - 1)
            }
        }

        rightArrow.setOnClickListener {
            val element = snapHelper.findSnapView(countryList.layoutManager) ?: throw Exception("Element not found")
            val position = countryList.layoutManager?.getPosition(element) ?: throw Exception("Position not found")
            if (position != countryListData.size) {
                countryList.scrollToPosition(position + 1)
            }
        }
    }
}
