package com.example.rovertestapp2.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rovertestapp2.R
import com.example.rovertestapp2.Models.Temperature

class MyAdapter: RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var temp : TextView = itemView.findViewById(R.id.recTemp)
        var time: TextView = itemView.findViewById(R.id.recTime)
    }

    private val tempList = ArrayList<Temperature>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_tempitem,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tempList.size
    }

    fun updateTempList(tempList: List<Temperature>) {
        Log.d("MyAdapter", "updateTempList called with list size: ${tempList.size}")
        this.tempList.clear()
        this.tempList.addAll(tempList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.temp.text = tempList[position].temp.toString()
        val timeText = tempList[position].time
        val realTime = timeText.replace("\"", "")
        holder.time.text = realTime
    }
}