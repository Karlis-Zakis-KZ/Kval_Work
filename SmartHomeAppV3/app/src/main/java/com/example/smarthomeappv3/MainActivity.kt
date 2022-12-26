package com.example.smarthomeappv3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeappv3.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
    }
    private fun replaceActivity(activity: AppCompatActivity){

        val i = Intent(this,activity::class.java)
        startActivity(i)
        finish()

    }
}