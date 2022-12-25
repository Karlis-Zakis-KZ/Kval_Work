package com.example.smarthomeappv3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeappv3.databinding.ActivityPricingBinding
import com.google.firebase.database.*


class PricingActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var priceRecyclerview : RecyclerView
    private lateinit var priceArrayList : ArrayList<PriceData>
    private lateinit var binding: ActivityPricingBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPricingBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        priceRecyclerview = findViewById(R.id.userList)
        priceRecyclerview.layoutManager = LinearLayoutManager(this)
        priceRecyclerview.setHasFixedSize(true)

        priceArrayList = arrayListOf<PriceData>()
        getUserData()

    }

    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("/Eletricity Prices/2022-12-25")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val price = userSnapshot.getValue(PriceData::class.java)
                        priceArrayList.add(price!!)
                    }
                    priceRecyclerview.adapter = MyAdapter(priceArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun replaceActivity(activity: AppCompatActivity){

        val i = Intent(this,activity::class.java)
        startActivity(i)
        finish()

    }
}