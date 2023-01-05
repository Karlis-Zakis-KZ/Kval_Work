package com.example.smarthomeappv3

import android.annotation.SuppressLint
import android.content.Intent
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
        holder.consumption.text = currentitem.Current_Consumption.toString()
        holder.state.isChecked = currentitem.On_Status!!


        holder.state.setOnClickListener{
            database.reference.child(devicePath).child("On_Status").setValue(holder.state.isChecked)
        }

        holder.deleteButton.setOnClickListener{
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
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val state : Switch = itemView.findViewById(R.id.stateSwitch)
        val deleteButton : ImageButton = itemView.findViewById(R.id.deleteDevice)
        val editButton : ImageButton = itemView.findViewById(R.id.editDevice)
    }
}