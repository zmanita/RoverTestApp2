package com.example.rovertestapp2

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rovertestapp2.Adapter.MyAdapter
import com.example.rovertestapp2.Adapter.MyAdapterH
import com.example.rovertestapp2.Models.HumidViewModel
import com.example.rovertestapp2.Models.TempViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LogFragment : Fragment() {
    private lateinit var dbTemp : DatabaseReference
    private lateinit var dbHumid : DatabaseReference
    private lateinit var adapter: MyAdapter
    private lateinit var adapterH: MyAdapterH
    private lateinit var viewModel : TempViewModel
    private lateinit var tempRecyclerView: RecyclerView
    private lateinit var viewModelHumid : HumidViewModel
    private lateinit var humidRecyclerView: RecyclerView

    var eventListener:ValueEventListener?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbTemp = FirebaseDatabase.getInstance().getReference("Temperature")
        dbHumid = FirebaseDatabase.getInstance().getReference("Humidity")

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_log, container, false)

        val resetTempBtn = view.findViewById<Button>(R.id.button)
        resetTempBtn.setOnClickListener {
            resetTemp()
        }
        val resetHumidBtn = view.findViewById<Button>(R.id.button2)
        resetHumidBtn.setOnClickListener {
            resetHumid()
        }

        //val backbtn = view.findViewById<Button>(R.id.button_back)
        //backbtn.setOnClickListener {
          //  val datafragment = com.example.rovertestapp2.DataFragment()
            //val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            //transaction.replace(R.id.container, datafragment)
            //transaction.commit()
        //}
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempRecyclerView = view.findViewById(R.id.recyclerTemp)
        tempRecyclerView.layoutManager = LinearLayoutManager(context)
        tempRecyclerView.setHasFixedSize(true)
        adapter = MyAdapter()
        tempRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(TempViewModel::class.java)
        viewModel.allTemps.observe(viewLifecycleOwner) { temps ->
            Log.d("DataLogFragment", "Observing LiveData with list size: ${temps.size}")
            adapter.updateTempList(temps)
        }
        humidRecyclerView = view.findViewById(R.id.recyclerHumid)
        humidRecyclerView.layoutManager = LinearLayoutManager(context)
        humidRecyclerView.setHasFixedSize(true)
        adapterH = MyAdapterH()
        humidRecyclerView.adapter = adapterH

        viewModelHumid = ViewModelProvider(this).get(HumidViewModel::class.java)
        viewModelHumid.allHumids.observe(viewLifecycleOwner) { humids ->
            Log.d("DataLogFragment", "Observing LiveData with list size: ${humids.size}")
            adapterH.updateHumidList(humids)
        }
    }
    fun resetTemp(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Confirm reset")
        builder.setMessage("Are you sure you want to reset all the temperature values?")
        builder.setPositiveButton("Yes") { dialog, id ->
            viewModel.deleteTemperatures()
            dialog.cancel()
        }
        builder.setNegativeButton("No"){ dialog, id ->
            dialog.cancel()
        }
        var alert = builder.create()
        alert.show()
    }
    fun resetHumid(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Confirm reset")
        builder.setMessage("Are you sure you want to reset all the humidity values?")
        builder.setPositiveButton("Yes") { dialog, id ->
            viewModelHumid.deleteHumidities()
            dialog.cancel()
        }
        builder.setNegativeButton("No"){ dialog, id ->
            dialog.cancel()
        }
        var alert = builder.create()
        alert.show()
    }

}
