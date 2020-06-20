package com.example.covidata

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_global_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GlobalData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_data)
        val layoutManager = LinearLayoutManager(this);
        val countryList = findViewById<RecyclerView>(R.id.CountryList)
        countryList.layoutManager = layoutManager
        countryList.setHasFixedSize(true)
        countryList.addItemDecoration(DividerItemDecoration(this@GlobalData, LinearLayoutManager.VERTICAL))

        val url = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofitClient = Retrofit.Builder().baseUrl(url).addConverterFactory(jsonConverter).build()
        val service = retrofitClient.create(WSInterface::class.java)

        val callbackGlobal:Callback<Global> = object : Callback<Global> {
            override fun onFailure(call: Call<Global>, t: Throwable) {
                Toast.makeText(applicationContext,"Couldn't access the API, check your internet connection", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Global>, response: Response<Global>) {
                if (response.code() == 200 && response.body() != null) {
                    val globalData: Global = response.body() as Global
                    globalDataTextView.text = "Confirmed: ${globalData.TotalConfirmed}\nDeaths: ${globalData.TotalDeaths}\nRecovered: ${globalData.TotalRecovered}"
                }
            }
        }

        val callbackCountries:Callback<List<Country>> = object : Callback<List<Country>> {
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(applicationContext,"Couldn't access the API, check your internet connection", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.code() == 200 && response.body() != null) {
                    val countryListData: MutableList<Country> = response.body() as MutableList<Country>
                    countryListData.sortWith( compareBy { it.Country })
                    countryList.adapter = CountryAdapter(this@GlobalData, countryListData)
                }
            }
        }

        service.getAllCountries().enqueue(callbackCountries)
        service.getGlobalData().enqueue(callbackGlobal)
    }
}
