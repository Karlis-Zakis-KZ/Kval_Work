package com.example.smarthomeappv3

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.net.InetAddress
import java.net.NetworkInterface
import com.example.smarthomeappv3.databinding.ActivityScanForDevicesIpBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.Socket

class ScanForDevicesIP : AppCompatActivity() {
    private lateinit var binding: ActivityScanForDevicesIpBinding
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanForDevicesIpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listView = findViewById(R.id.listForIPAddresses)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        val scanButton : Button = findViewById(R.id.buttonScanForDevices)
        scanButton.setOnClickListener{
            scanNetwork()
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

    private fun scanNetwork(){
        val subnet = "192.168.1."
        for (i in 50..170) {
            Thread {
                val host = subnet + i
                try {
                    val socket = Socket(host, 80)
                    println("$host is open on port 80")
                    runOnUiThread {adapter.add(host)}
                    socket.close()
                } catch (e: IOException) {
                    println("$host is not open on port 80")
                }
            }.start()
        }
    }

    private fun replaceActivity(activity: AppCompatActivity){
        val i = Intent(this,activity::class.java)
        startActivity(i)
        finish()
    }
}
