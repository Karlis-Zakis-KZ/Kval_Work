package com.example.smarthomeappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.smarthomeappv3.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val mCurrentUserId: String = mAuth.currentUser!!.uid
        val mCurrentUserEmail: String? = mAuth.currentUser!!.email

        val userPageEmail : TextView = findViewById<TextView>(R.id.userPageEmail)
        val userPageUUID : TextView = findViewById<TextView>(R.id.userPageUUID)

        userPageEmail.text = mCurrentUserEmail
        userPageUUID.text = mCurrentUserId

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