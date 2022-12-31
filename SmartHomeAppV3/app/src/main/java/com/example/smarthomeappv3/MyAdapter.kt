package com.example.smarthomeappv3

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val deviceList : ArrayList<DeviceData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = deviceList[position]

        holder.name.text = currentitem.Device_Name.toString()
        holder.consumption.text = currentitem.Current_Consumption.toString()
        holder.state.isChecked = currentitem.On_Status!!

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.displayDeviceName)
        val consumption : TextView = itemView.findViewById(R.id.displayConsumption)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val state : Switch = itemView.findViewById(R.id.stateSwitch)

    }

}