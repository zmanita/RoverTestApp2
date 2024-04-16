package com.example.rovertestapp2.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rovertestapp2.Models.Humidity
import com.example.rovertestapp2.R


class MyAdapterH: RecyclerView.Adapter<MyAdapterH.MyViewHolder>() {
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var humid : TextView = itemView.findViewById(R.id.recHumid)
        var time: TextView = itemView.findViewById(R.id.recTimeH)
    }

    private val humidList = ArrayList<Humidity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_humiditem,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return humidList.size
    }

    fun updateHumidList(humidList: List<Humidity>){
        Log.d("MyAdapter", "updateHumidList called with list size: ${humidList.size}")
        this.humidList.clear()
        this.humidList.addAll(humidList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.humid.text = humidList[position].humid.toString()
        val timeText = humidList[position].time
        val realTime = timeText.replace("\"", "")
        holder.time.text = realTime
    }
}