package com.example.rovertestapp2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.example.rovertestapp2.Models.Temperature
import com.example.rovertestapp2.Models.humidity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class DataFragment : Fragment() {

    private lateinit var dbTemp :DatabaseReference
    private lateinit var dbHumid :DatabaseReference
    var newTemp = 24
    var newHumid = 40

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbTemp = FirebaseDatabase.getInstance().getReference("temperature")
        dbHumid = FirebaseDatabase.getInstance().getReference("humidity")
    }


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_data, container, false)
        val logbtn = view.findViewById<Button>(R.id.button_back)
        logbtn.setOnClickListener {
            val logfragment = com.example.rovertestapp2.LogFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
           transaction.replace(R.id.container,logfragment)
            transaction.commit()
        }
        val tempbtn = view.findViewById<Button>(R.id.temp_btn)
        val textViewTemp = view.findViewById<TextView>(R.id.textViewTemp)
        val textViewTempTime = view.findViewById<TextView>(R.id.textViewTempTime)
        tempbtn.setOnClickListener {
            dbTemp.child("temp").get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                textViewTemp.text = " ${it.value}°C"
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
            dbTemp.child("time").get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                textViewTempTime.text = " ${it.value} h"
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }
        val humidbtn = view.findViewById<Button>(R.id.humid_btn)
        val textViewHumid = view.findViewById<TextView>(R.id.textViewHumid)
        val textViewHumidT = view.findViewById<TextView>(R.id.textViewHumidT)
        humidbtn.setOnClickListener {
            dbHumid.child("humid").get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                textViewHumid.text = " ${it.value}%"
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
            dbHumid.child("time").get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                textViewHumidT.text = " ${it.value} h"
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

        return view
        }

    fun writeNewTemp(temp: Int, time: String) {
        val temp = Temperature(temp, time)
        dbTemp.setValue(temp)
    }
    fun writeNewHumid(humid: Int, time: String) {
        val humid = humidity(humid, time)
        dbHumid.setValue(humid)
    }
    fun getCurrentTime(): String {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        var minute = c.get(Calendar.MINUTE)
        var current ="$hour:$minute"
        return current
    }
    fun readTemp() {
        dbTemp.child("temp").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val t = it.value
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
    fun readTempTime(){
        dbTemp.child("time").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }

}
