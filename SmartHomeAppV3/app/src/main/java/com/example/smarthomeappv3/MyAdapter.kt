package com.example.smarthomeappv3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val priceList : ArrayList<PriceData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.price_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = priceList[position]

        //holder.color.text = currentitem.Color.toString()
        //holder.price.text = currentitem.Price.toString()

    }

    override fun getItemCount(): Int {

        return priceList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        //val color : TextView = itemView.findViewById(R.id.tvfirstName)
        //val price : TextView = itemView.findViewById(R.id.tvage)

    }

}