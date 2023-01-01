package com.example.smarthomeappv3

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeappv3.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var binding: ActivityMainBinding
    private lateinit var deviceRecyclerview : RecyclerView
    private lateinit var deviceArrayList : ArrayList<DeviceData>



    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val mCurrentUserId: String = mAuth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance()

        val userTemp = database.getReference("/Users/" + mCurrentUserId + "/Temp")
        userTemp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val humidValue= dataSnapshot.child("Humidity").value
                val tempValue= dataSnapshot.child("Tempeture").value

                binding.CurrentTemp.text = tempValue.toString()
                binding.CurrentHumid.text = humidValue.toString()

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        deviceRecyclerview = findViewById(R.id.deviceList)
        deviceRecyclerview.layoutManager = LinearLayoutManager(this)
        deviceRecyclerview.setHasFixedSize(true)

        deviceArrayList = arrayListOf<DeviceData>()
        getUserData(mCurrentUserId)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        bottomNavigationView.selectedItemId = R.id.home

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

        val buttonClick = findViewById<MaterialButton>(R.id.addDeviceButton)
        buttonClick.setOnClickListener {
            replaceActivity(AddDevice())
        }

    }

    private fun replaceActivity(activity: AppCompatActivity){

        val i = Intent(this,activity::class.java)
        startActivity(i)
        finish()

    }

    private fun getUserData(mCurrentUserId: String) {
        if(deviceArrayList.isNotEmpty()){
            deviceArrayList.clear()
        }
        dbref = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUserId + "/Devices")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (deviceSnapshot in snapshot.children){
                        val device = deviceSnapshot.getValue(DeviceData::class.java)

                        if (device != null) {
                            val positionTest = deviceArrayList.indexOfFirst { it.Device_ID == device.Device_ID }

                            if (positionTest >= 0 ){
                                deviceArrayList[positionTest] = device
                            }else{
                                deviceArrayList.add(device)
                            }
                        }
                    }
                    deviceRecyclerview.adapter = MyAdapter(deviceArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}