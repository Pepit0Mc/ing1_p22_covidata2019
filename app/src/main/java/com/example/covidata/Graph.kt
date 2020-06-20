package com.example.covidata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_graph.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Graph : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var status = "confirmed"
        var countryStat: List<CountryStat> = emptyList()
        var countryListData: MutableList<Country> = mutableListOf()

        val countryList = findViewById<RecyclerView>(R.id.CountryList)
        countryList.setHasFixedSize(true)
        countryList.addItemDecoration(DividerItemDecoration(this@Graph, LinearLayoutManager.HORIZONTAL))
        countryList.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(countryList)

        val layoutManagerVertical = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val graph = findViewById<RecyclerView>(R.id.graph)
        graph.layoutManager = layoutManagerVertical

        val url = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofitClient = Retrofit.Builder().baseUrl(url).addConverterFactory(jsonConverter).build()
        val service = retrofitClient.create(WSInterface::class.java)

        fun displayGraph() {
            if (status.equals("confirmed"))
                graph.adapter = GraphConfirmedAdapter(this@Graph, countryStat)
            else if (status.equals("deaths"))
                graph.adapter = GraphDeathsAdapter(this@Graph, countryStat)
            else if (status.equals("recovered"))
                graph.adapter = GraphRecoveredAdapter(this@Graph, countryStat)
        }

        radioButtonConfirmed.setOnClickListener {
            status = "confirmed"
            displayGraph()
        }

        radioButtonDeaths.setOnClickListener {
            status = "deaths"
            displayGraph()
        }

        radioButtonRecovered.setOnClickListener {
            status = "recovered"
            displayGraph()
        }

        val callbackCountryData: Callback<List<CountryStat>> = object : Callback<List<CountryStat>> {
            override fun onFailure(call: Call<List<CountryStat>>, t: Throwable) {
                Toast.makeText(applicationContext,"Couldn't access the API, check your internet connection", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<CountryStat>>, response: Response<List<CountryStat>>) {
                if (response.code() == 200 && response.body() != null) {
                    countryStat = response.body() as List<CountryStat>
                    if (countryStat.size == 0)
                        Toast.makeText(applicationContext,"No data for this country", Toast.LENGTH_SHORT).show()
                    displayGraph()
                }
            }
        }

        val callbackCountries: Callback<List<Country>> = object : Callback<List<Country>> {
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(applicationContext,"Couldn't access the API, check your internet connection", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.code() == 200 && response.body() != null) {
                    countryListData = response.body() as MutableList<Country>
                    countryListData.sortWith( compareBy { it.Country })
                    countryList.adapter = CountryAdapter(this@Graph, countryListData)
                    service.getCountryAllData(countryListData[0].Slug).enqueue(callbackCountryData)
                }
            }
        }

        service.getAllCountries().enqueue(callbackCountries)


        val onScrollListener = object:RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val element = snapHelper.findSnapView(recyclerView.layoutManager) ?: return
                val position = recyclerView.layoutManager?.getPosition(element) ?: return
                val country = countryListData[position].Slug
                service.getCountryAllData(country).enqueue(callbackCountryData)
            }
        }

        leftArrow.setOnClickListener {
            val element = snapHelper.findSnapView(countryList.layoutManager) ?: throw Exception("Element not found")
            val position = countryList.layoutManager?.getPosition(element) ?: throw Exception("Position not found")
            if (position != 0) {
                countryList.scrollToPosition(position - 1)
                service.getCountryAllData(countryListData[position - 1].Slug).enqueue(callbackCountryData)
            }
        }

        rightArrow.setOnClickListener {
            val element = snapHelper.findSnapView(countryList.layoutManager) ?: throw Exception("Element not found")
            val position = countryList.layoutManager?.getPosition(element) ?: throw Exception("Position not found")
            if (position < countryListData.size) {
                countryList.scrollToPosition(position + 1)
                service.getCountryAllData(countryListData[position + 1].Slug).enqueue(callbackCountryData)
            }
        }

        countryList.addOnScrollListener(onScrollListener)
    }
}
