package com.example.smarthomeappv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.smarthomeappv2.databinding.ActivityLoginBinding
import com.example.smarthomeappv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())


        binding.bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.Pricing -> replaceFragment(PricingFragment())
                R.id.User -> replaceFragment(UserFragment())
                else -> {
                }
            }
            true
        }
        val buttonClick = findViewById<Button>(R.id.mainPageSignUp)
        buttonClick.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()

        fragmentTransition.replace(R.id.fragment_container,fragment)
        fragmentTransition.commit()


    }

}