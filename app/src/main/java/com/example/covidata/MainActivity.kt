package com.example.covidata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataBtn = findViewById<Button>(R.id.dataBtn)
        dataBtn.setOnClickListener{
            val intent = Intent(this, GlobalData::class.java)
            startActivity(intent)
        }

        val graphBtn = findViewById<Button>(R.id.graphBtn)
        graphBtn.setOnClickListener {
            val intent = Intent(this, Graph::class.java)
            startActivity(intent)
        }
    }
}
