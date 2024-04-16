package com.example.rovertestapp2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.rovertestapp2.Models.Humidity
import com.github.mikephil.charting.charts.LineChart
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates

class ControlFragment : Fragment() {
    private var movInt : Int = 0
    private lateinit var dbMov : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbMov = FirebaseDatabase.getInstance().getReference("Movement")
        val view = inflater.inflate(R.layout.fragment_control, container, false)
        val buttonRight = view.findViewById<Button>(R.id.buttonRight)
        buttonRight.setOnClickListener {
            movInt = 0
            writeMov(movInt)
        }
        val buttonLeft = view.findViewById<Button>(R.id.buttonLeft)
        buttonLeft.setOnClickListener {
            movInt = 1
            writeMov(movInt)
        }
        val buttonBackwards = view.findViewById<Button>(R.id.buttonBackwards)
        buttonBackwards.setOnClickListener {
            movInt = 2
            writeMov(movInt)
        }
        val buttonForward = view.findViewById<Button>(R.id.buttonForward)
        buttonForward.setOnClickListener {
            movInt = 3
            writeMov(movInt)
        }
        val buttonStop = view.findViewById<Button>(R.id.buttonStop)
        buttonStop.setOnClickListener {
            movInt = 4
            writeMov(movInt)
        }
        return view
    }

    fun writeMov(movInt: Int){
        dbMov.setValue(movInt)
    }

}