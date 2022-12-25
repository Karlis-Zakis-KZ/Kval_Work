package com.example.smarthomeappv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val priceList : ArrayList<Prices>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.price_item,
        parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        
        val currentitem = priceList[position]

        holder.color.text = currentitem.color
        holder.price.text = currentitem.price

    }

    override fun getItemCount(): Int {
        return priceList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val color : TextView = itemView.findViewById(R.id.tvfirstName)
        val price : TextView = itemView.findViewById(R.id.tvlastName)

    }
}