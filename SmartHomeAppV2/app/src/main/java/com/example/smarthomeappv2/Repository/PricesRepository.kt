package com.example.smarthomeappv2.Repository

import androidx.lifecycle.MutableLiveData
import com.example.smarthomeappv2.Models.Prices
import com.google.firebase.database.*

class PricesRepository {


    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Todays Prices")

    @Volatile private var INSTANCE : PricesRepository ?= null

    fun getInstace() : PricesRepository{
        return INSTANCE ?: synchronized(this){
            val instance = PricesRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadPrices(priceList : MutableLiveData<List<Prices>>){

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _userList : List<Prices> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Prices::class.java)!!
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}