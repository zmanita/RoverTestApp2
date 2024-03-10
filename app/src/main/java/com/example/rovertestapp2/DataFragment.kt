package com.example.rovertestapp2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

class DataFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        tempbtn.setOnClickListener {
            textViewTemp.text = "New Temp"
        }
        val humidbtn = view.findViewById<Button>(R.id.humid_btn)
        val textViewHumid = view.findViewById<TextView>(R.id.textViewHumid)
        humidbtn.setOnClickListener {
            textViewHumid.text = "New Humid"
        }

        return view
        }



}
