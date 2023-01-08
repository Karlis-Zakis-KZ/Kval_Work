package com.example.smarthomeappv3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeappv3.databinding.ActivityConfigureDeviceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ConfigureDevice : AppCompatActivity() {
    private lateinit var binding: ActivityConfigureDeviceBinding
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongViewCast", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deviceID = intent.getStringExtra("deviceID")
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val mCurrentUserId: String = mAuth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance()
        val devicePath = "Users/$mCurrentUserId/Devices/$deviceID"

        binding = ActivityConfigureDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val simpleTimePicker: TimePicker = findViewById(R.id.timePicker1)
        val tempButton: ToggleButton = findViewById(R.id.tempSelect)
        val humidButton: ToggleButton = findViewById(R.id.humidSelect)
        val timeButton: ToggleButton = findViewById(R.id.timeSelect)
        val priceButton: ToggleButton = findViewById(R.id.priceSelect)

        val disableTempInput: EditText = findViewById(R.id.inputTargetTemp)
        val disableHumidInput: EditText = findViewById(R.id.inputTargetHumid)
        val disableTargetPrice: EditText = findViewById(R.id.inputTargetPrice)
        val saveButton : Button = findViewById(R.id.saveDeviceInfo)


        val deviceName: EditText = findViewById(R.id.inputDeviceName)
        val deviceIpText: EditText = findViewById(R.id.inputDeviceIP)
        database.reference.child(devicePath).get().addOnSuccessListener {

            deviceName.setText(it.child("Device_Name").value.toString())
            deviceIpText.setText(it.child("Device_IP").value.toString())
            tempButton.isChecked = it.child("HasTemp").value as Boolean
            humidButton.isChecked = it.child("HasHumid").value as Boolean
            timeButton.isChecked = it.child("HasTime").value as Boolean
            priceButton.isChecked = it.child("HasPrice").value as Boolean

            if(!tempButton.isChecked){ disableTempInput.isEnabled = tempButton.isChecked }
            if(!humidButton.isChecked){ disableHumidInput.isEnabled = humidButton.isChecked }
            if(!timeButton.isChecked){ simpleTimePicker.isEnabled = timeButton.isChecked }
            if(!priceButton.isChecked){ disableTargetPrice.isEnabled = priceButton.isChecked }

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }


        simpleTimePicker.setIs24HourView(true)
        priceButton.setOnCheckedChangeListener { _, _ ->
            disableTargetPrice.isEnabled = priceButton.isChecked
        }
        tempButton.setOnCheckedChangeListener { _, _ ->
            disableTempInput.isEnabled = tempButton.isChecked
        }
        humidButton.setOnCheckedChangeListener { _, _ ->
            disableHumidInput.isEnabled = humidButton.isChecked
        }
        timeButton.setOnCheckedChangeListener { _, _ ->
            simpleTimePicker.isEnabled = timeButton.isChecked
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


        saveButton.setOnClickListener {
            if(deviceName.toString().isNotEmpty() && deviceIpText.toString().isNotEmpty()){
                if((tempButton.isChecked && disableTempInput.toString().isNotEmpty()) ||
                    (humidButton.isChecked && disableHumidInput.toString().isNotEmpty()) ||
                    (timeButton.isChecked && simpleTimePicker.toString().isNotEmpty()))
                {
                    database.reference.child(devicePath).child("Device_Name").setValue(deviceName.text.toString())
                    database.reference.child(devicePath).child("Device_IP").setValue(deviceIpText.text.toString())
                    database.reference.child(devicePath).child("HasTime").setValue(timeButton.isChecked)
                    database.reference.child(devicePath).child("HourSet").setValue(simpleTimePicker.hour)
                    database.reference.child(devicePath).child("MinSet").setValue(simpleTimePicker.minute)
                    database.reference.child(devicePath).child("HasTemp").setValue(tempButton.isChecked)
                    database.reference.child(devicePath).child("TempSet").setValue(disableTempInput.text.toString())
                    database.reference.child(devicePath).child("HasHumid").setValue(humidButton.isChecked)
                    database.reference.child(devicePath).child("HumidSet").setValue(disableHumidInput.text.toString())
                    database.reference.child(devicePath).child("HasPrice").setValue(priceButton.isChecked)
                    database.reference.child(devicePath).child("PriceSet").setValue(disableTargetPrice.text.toString())
                    replaceActivity(MainActivity())
                }else{
                    Toast.makeText(this, "Pleas fill out the selected events for the device to turn off to", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Name and IP cant be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun replaceActivity(activity: AppCompatActivity){

        val i = Intent(this,activity::class.java)
        startActivity(i)
        finish()
    }

}