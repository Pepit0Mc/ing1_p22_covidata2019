package com.example.covidata

import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_country_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CountryData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_data)

        val country : String = intent.getStringExtra(COUNTRY)
        val countryTextView = findViewById<TextView>(R.id.country_textView)
        countryTextView.text = country
        val url = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofitClient = Retrofit.Builder().baseUrl(url).addConverterFactory(jsonConverter).build()
        val service = retrofitClient.create(WSInterface::class.java)

        val callbackCountry: Callback<List<CountryStat>> = object : Callback<List<CountryStat>> {
            override fun onFailure(call: Call<List<CountryStat>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<List<CountryStat>>, response: Response<List<CountryStat>>) {
                if (response.code() == 200 && response.body() != null) {
                    val countryData: List<CountryStat> = response.body() as List<CountryStat>
                    val dateData: CountryStat = countryData[0]
                    countryTextView.text = dateData.Country
                    if (countryData.size > 1)
                        StatCountryTextView.text = "No data available"
                    else
                        StatCountryTextView.text = "Confirmed: ${dateData.Confirmed}\nDeaths: ${dateData.Deaths}\nRecovered: ${dateData.Recovered}"
                }
            }
        }

        calendarView.setOnDateChangeListener{ calendarView: CalendarView, i: Int, i1: Int, i2: Int ->
            val date = "" + i + "-" +  (if (i1 + 1 < 10) "0" else "") + (i1 + 1) + "-" + (if (i2 < 10) "0" else "") + i2 + "T"
            Toast.makeText(applicationContext, date, Toast.LENGTH_SHORT).show()
            service.getCountryData(country, date + "00:00:00Z", date + "23:59:59Z").enqueue(callbackCountry)
        }
    }
}
