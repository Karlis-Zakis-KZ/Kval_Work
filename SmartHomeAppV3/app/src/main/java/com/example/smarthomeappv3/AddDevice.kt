package com.example.smarthomeappv3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeappv3.databinding.ActivityAddDeviceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AddDevice : AppCompatActivity() {

    private lateinit var binding: ActivityAddDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val mCurrentUserId: String = mAuth.currentUser!!.uid

        val saveDevice = findViewById<Button>(R.id.saveDeviceInfo)
        saveDevice.setOnClickListener {
            val deviceName = binding.inputDeviceName.text.toString()
            val deviceID = binding.inputDeviceID.text.toString()
            val deviceIP = binding.inputDeviceIP.text.toString()

            val database = FirebaseDatabase.getInstance()

            val devicePath = "Users/$mCurrentUserId/Devices/$deviceID"

            database.reference.child(devicePath).child("Device_ID").setValue(deviceID)
            database.reference.child(devicePath).child("Device_Name").setValue(deviceName)
            database.reference.child(devicePath).child("On_Status").setValue("false")
            database.reference.child(devicePath).child("Consumed_Energy_AtStart").setValue(0)
            database.reference.child(devicePath).child("Current_Consumption").setValue(0)
            database.reference.child(devicePath).child("Last_Calculated_Consumption").setValue(0)
            database.reference.child(devicePath).child("Calculated_Price").setValue(0)
            database.reference.child(devicePath).child("Device_IP").setValue(deviceIP)
            replaceActivity(MainActivity())

        }

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