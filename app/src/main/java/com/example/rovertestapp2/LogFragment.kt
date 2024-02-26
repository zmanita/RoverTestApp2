package com.example.rovertestapp2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction

class LogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_log, container, false)

        val backbtn = view.findViewById<Button>(R.id.button_back)
        backbtn.setOnClickListener {
            val datafragment = com.example.rovertestapp2.DataFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, datafragment)
            transaction.commit()
        }
        return view
    }
}