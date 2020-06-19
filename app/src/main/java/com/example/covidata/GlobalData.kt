package com.example.covidata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GlobalData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_data)
        val layoutManager = LinearLayoutManager(this);

        val countryList = findViewById<RecyclerView>(R.id.CountryList);
        val countryListData : MutableList<String> = arrayListOf("France", "Spain", "England")
        countryList.layoutManager = layoutManager
        countryList.setHasFixedSize(true)
        countryList.addItemDecoration(DividerItemDecoration(this@GlobalData, LinearLayoutManager.VERTICAL))
        countryList.adapter = CountryAdapter(this@GlobalData, countryListData)
    }
}
