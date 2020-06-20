package com.example.covidata

import android.content.DialogInterface
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_country_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class CountryData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_data)

        val country : String = intent.getStringExtra(COUNTRY)
        val countryName : String = intent.getStringExtra(COUNTRYNAME)
        val countryTextView = findViewById<TextView>(R.id.country_textView)
        countryTextView.text = countryName
        val url = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofitClient = Retrofit.Builder().baseUrl(url).addConverterFactory(jsonConverter).build()
        val service = retrofitClient.create(WSInterface::class.java)

        val callbackCountry: Callback<List<CountryStat>> = object : Callback<List<CountryStat>> {
            override fun onFailure(call: Call<List<CountryStat>>, t: Throwable) {
                Toast.makeText(applicationContext,"Couldn't access the API, check your internet connection", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<CountryStat>>, response: Response<List<CountryStat>>) {
                if (response.code() == 200 && response.body() != null) {
                    val countryData: List<CountryStat> = response.body() as List<CountryStat>

                    if (countryData.size != 1)
                        StatCountryTextView.text = "No data available"
                    else {
                        val dateData: CountryStat = countryData[0]
                        StatCountryTextView.text =
                            "Confirmed: ${dateData.Confirmed}\nDeaths: ${dateData.Deaths}\nRecovered: ${dateData.Recovered}"
                    }
                }
            }
        }

        val DatechangeListener = CalendarView.OnDateChangeListener{ _: CalendarView, i: Int, i1: Int, i2: Int ->
            val date = "" + i + "-" +  (if (i1 + 1 < 10) "0" else "") + (i1 + 1) + "-" + (if (i2 < 10) "0" else "") + i2 + "T"
            service.getCountryData(country, date + "00:00:00Z", date + "23:59:59Z").enqueue(callbackCountry)
        }

        calendarView.setOnDateChangeListener(DatechangeListener)
        DatechangeListener.onSelectedDayChange(calendarView,Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH)
    }
}
