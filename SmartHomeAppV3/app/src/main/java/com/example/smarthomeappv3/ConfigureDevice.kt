package com.example.smarthomeappv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker

class ConfigureDevice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_device)

        val simpleTimePicker: TimePicker = findViewById(R.id.timePicker1)
        simpleTimePicker.setIs24HourView(true)

    }
}