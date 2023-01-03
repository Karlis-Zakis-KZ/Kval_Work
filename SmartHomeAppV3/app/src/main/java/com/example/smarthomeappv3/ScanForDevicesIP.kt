package com.example.smarthomeappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.smarthomeappv3.databinding.ActivityScanForDevicesIpBinding
import java.net.InetAddress

class ScanForDevicesIP : AppCompatActivity() {
    private lateinit var binding: ActivityScanForDevicesIpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanForDevicesIpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val findIPAddresses: Button = findViewById(R.id.buttonScanForDevices)
        findIPAddresses.setOnClickListener{
            ///scanIpRange("192.168.1.109", "192.168.1.111")
            ScanTask().execute("192.168.1.99", "192.168.1.115")
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


    private fun scanIpRange(startIp: String, endIp: String) {
        val start = startIp.split(".")
        val end = endIp.split(".")
        for (i in start[0].toInt()..end[0].toInt()) {
            for (j in start[1].toInt()..end[1].toInt()) {
                for (k in start[2].toInt()..end[2].toInt()) {
                    for (l in start[3].toInt()..end[3].toInt()) {
                        val address = "$i.$j.$k.$l"
                        Thread {
                            val inet = InetAddress.getByName(address)
                            if (inet.isReachable(10000)) {
                                println("$address is reachable")
                            }else{
                                println("$address is not reachable")
                            }
                        }.start()
                    }
                }
            }
        }
    }

    private fun replaceActivity(activity: AppCompatActivity){

        val i = Intent(this,activity::class.java)
        startActivity(i)
        finish()

    }
}