package com.example.smarthomeappv3

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeappv3.databinding.ActivityPricingBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import java.time.LocalDate


class PricingActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var priceRecyclerview : RecyclerView
    private lateinit var priceArrayList : ArrayList<PriceData>
    private lateinit var binding: ActivityPricingBinding
    private lateinit var barChart: BarChart
    private lateinit var barData: BarData
    private lateinit var barDataSet: BarDataSet
    private var barEntriesList = ArrayList<BarEntry>()

    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDate.now().toString()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPricingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        bottomNavigationView.selectedItemId = R.id.Pricing

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

        priceArrayList = arrayListOf()
        getUserData()



    }

    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("/Eletricity Prices/"+ current)
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val price = userSnapshot.getValue(PriceData::class.java)
                        priceArrayList.add(price!!)
                    }
                    //priceRecyclerview.adapter = MyAdapter(priceArrayList)
                }

                val rightNow = Calendar.getInstance()
                val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY)

                binding.currentPricePrice.text = priceArrayList[currentHourIn24Format].Price.toString()

                setBarChart()
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

    private fun setBarChart() {
        barChart = findViewById(R.id.barChart)

        // on below line we are calling get bar
        // chart data to add data to our array list
        getBarChartData()

        // on below line we are initializing our bar data set
        barDataSet = BarDataSet(barEntriesList, "Data taken from : https://nordpool.didnt.work/?vat")

        // on below line we are initializing our bar data
        barData = BarData(barDataSet)

        // on below line we are setting data to our bar chart
        barChart.data = barData

        // on below line we are setting colors for our bar chart text
        barDataSet.valueTextColor = Color.BLACK

        // on below line we are setting color for our bar data set
        barDataSet.setColor(resources.getColor(R.color.purple_200))

        // on below line we are setting text size
        barDataSet.valueTextSize = 16f

        // on below line we are enabling description as false
        barChart.description.isEnabled = false

        barChart.invalidate()

    }

    private fun getBarChartData() {
        barEntriesList = ArrayList()

        for (price in priceArrayList){
            val tempTime = price.Time!!
            val tempPrice = price.Price!!
            barEntriesList.add(BarEntry(tempTime,tempPrice))
        }
    }

}