package com.example.covidata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CountryData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_data)

        val country : String = intent.getStringExtra(COUNTRY)
        val country_text_view = findViewById<TextView>(R.id.country_textView)
        country_text_view.text = country
    }
}
