package com.example.smarthomeappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.smarthomeappv3.databinding.ActivityAddDeviceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddDevice : AppCompatActivity() {

    private lateinit var firebaseData: DatabaseReference
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

            val database = FirebaseDatabase.getInstance()

            database.reference.child("Users/$mCurrentUserId/$deviceID").child("Device_ID").setValue(deviceID)
            database.reference.child("Users/$mCurrentUserId/$deviceID").child("Device_Name").setValue(deviceName)
            database.reference.child("Users/$mCurrentUserId/$deviceID").child("On_Status").setValue("False")
            database.reference.child("Users/$mCurrentUserId/$deviceID").child("Consumed_Energy_AtStart").setValue(0)
            database.reference.child("Users/$mCurrentUserId/$deviceID").child("Current_Consumption").setValue(0)
            database.reference.child("Users/$mCurrentUserId/$deviceID").child("Last_Calculated_Consumption").setValue(0)
            database.reference.child("Users/$mCurrentUserId/$deviceID").child("Calculated_Price").setValue(0)

            replaceActivity(MainActivity())

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