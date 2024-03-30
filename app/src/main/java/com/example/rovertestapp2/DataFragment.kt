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
import com.example.rovertestapp2.Models.Humidity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class DataFragment : Fragment() {

    private lateinit var dbTemp :DatabaseReference
    private lateinit var dbHumid :DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbTemp = FirebaseDatabase.getInstance().getReference("Temperature")
        dbHumid = FirebaseDatabase.getInstance().getReference("Humidity")
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
            getLastTemp(textViewTemp, textViewTempTime)
        }
        val humidbtn = view.findViewById<Button>(R.id.humid_btn)
        val textViewHumid = view.findViewById<TextView>(R.id.textViewHumid)
        val textViewHumidT = view.findViewById<TextView>(R.id.textViewHumidT)
        humidbtn.setOnClickListener {
            getLastHumid(textViewHumid,textViewHumidT)
        }

        return view
        }

    fun getLastTemp(textViewTemp: TextView,textViewTempTime: TextView){
        dbTemp.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val time = childSnapshot.key
                        val temp = childSnapshot.child("Temperature").getValue(Int::class.java)

                        Log.i("firebase", "Latest Time: $time, Temperature: $temp")
                        textViewTemp.text = "$temp°C"
                        textViewTempTime.text = "$time h"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error fetching data", error.toException())
            }
        })
    }
    fun getLastHumid(textViewHumid: TextView,textViewHumidTime: TextView){
        dbHumid.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val time = childSnapshot.key
                        val humid = childSnapshot.child("Humidity").getValue(Int::class.java)

                        Log.i("firebase", "Latest Time: $time, Humidity: $humid")
                        textViewHumid.text = "$humid %"
                        textViewHumidTime.text = "$time h"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Error fetching data", error.toException())
            }
        })
    }
    fun writeNewTemp( time: String, temp: Int) {
        val temp = Temperature(time, temp)
        dbTemp.setValue(temp)
    }
    fun writeNewHumid(humid: Int, time: String) {
        val humid = Humidity(time, humid)
        dbHumid.setValue(humid)
    }
    fun getCurrentTime(): String {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        var minute = c.get(Calendar.MINUTE)
        var current ="$hour:$minute"
        return current
    }

}
