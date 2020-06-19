package com.example.covidata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataBtn = findViewById<Button>(R.id.dataBtn)
        dataBtn.setOnClickListener(View.OnClickListener() {
            val intent = Intent(this, GlobalData::class.java)
            startActivity(intent)
        })
    }
}
