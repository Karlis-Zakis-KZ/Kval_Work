package com.example.smarthomeappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TimePicker
import com.example.smarthomeappv3.databinding.ActivityConfigureDeviceBinding
import com.example.smarthomeappv3.databinding.ActivityScanForDevicesIpBinding


class ConfigureDevice : AppCompatActivity() {
    private lateinit var binding: ActivityConfigureDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigureDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val simpleTimePicker: TimePicker = findViewById(R.id.timePicker1)
        simpleTimePicker.setIs24HourView(true)

        val findIPAddresses: ImageButton = findViewById(R.id.scanNetwork)
        findIPAddresses.setOnClickListener{
            replaceActivity(ScanForDevicesIP())
        }

        binding.bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceActivity(MainActivity())
                R.id.Pricing -> replaceActivity(PricingActivity())
                R.id.User -> replaceActivity(UserActivity())
                else -> {
                }
            }
            true
        }

    }

    private fun replaceActivity(activity: AppCompatActivity){

        val i = Intent(this,activity::class.java)
        startActivity(i)
        finish()

    }
}