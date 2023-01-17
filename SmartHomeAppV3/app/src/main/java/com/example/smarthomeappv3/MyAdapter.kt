package com.example.smarthomeappv3

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MyAdapter(private val deviceList : MutableList<DeviceData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private lateinit var firebaseData: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_item,
            parent,false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = deviceList[position]
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val mCurrentUserId: String = mAuth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance()
        val deviceID = currentitem.Device_ID
        val devicePath = "Users/$mCurrentUserId/Devices/$deviceID"

        holder.name.text = currentitem.Device_Name.toString()
        val tempTextWatt = currentitem.Current_Consumption.toString()+"W"
        holder.consumption.text = tempTextWatt
        val tempTextPrice = currentitem.Calculated_Price.toString()+"EUR"
        holder.price.text = tempTextPrice
        holder.state.isChecked = currentitem.On_Status!!.toBoolean()

        Log.d("firebase", currentitem.On_Status!!.toBoolean().toString())


        holder.state.setOnClickListener{
            database.reference.child(devicePath).child("On_Status").setValue(holder.state.isChecked.toString())
        }

        holder.deleteButton.setOnClickListener{

            database.reference.child("Users/$mCurrentUserId/DeviceList/DeviceIDS").get().addOnSuccessListener {
                val tempString = it.value.toString()
                val modifiedString = deviceID?.let { it1 -> tempString.replace(it1+",", "") }
                database.reference.child("Users/$mCurrentUserId/DeviceList/DeviceIDS").setValue(modifiedString)
            }

            database.reference.child(devicePath).removeValue()
            deviceList.removeAt(position)
        }

        holder.editButton.setOnClickListener { v ->
            val intent = Intent(v.context, ConfigureDevice::class.java)
            intent.putExtra("deviceID",currentitem.Device_ID)
            v.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }



    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.displayDeviceName)
        val consumption : TextView = itemView.findViewById(R.id.displayConsumption)
        val price : TextView = itemView.findViewById(R.id.displayCalculatedPrice)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val state : Switch = itemView.findViewById(R.id.stateSwitch)
        val deleteButton : ImageButton = itemView.findViewById(R.id.deleteDevice)
        val editButton : ImageButton = itemView.findViewById(R.id.editDevice)
    }
}