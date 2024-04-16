package com.example.rovertestapp2

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.fragment.app.FragmentTransaction
import com.example.rovertestapp2.Models.Temperature
import com.example.rovertestapp2.Models.Humidity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.ParseException
import java.util.*
import java.text.SimpleDateFormat
import java.util.Locale

class DataFragment : Fragment() {

    private lateinit var dbTemp :DatabaseReference
    private lateinit var dbHumid :DatabaseReference
    private lateinit var lineChart: LineChart
    private lateinit var lineChartH: LineChart

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
        val view = inflater.inflate(R.layout.fragment_data, container, false)
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

        lineChart = view.findViewById(R.id.chart)
        lineChartH = view.findViewById(R.id.chartH)
        setupTemperatureChart()
        listenForTemperatureUpdates()
        setupHumidityChart()
        listenForHumidityUpdates()

        return view
        }


    fun getLastTemp(textViewTemp: TextView,textViewTempTime: TextView){
        dbTemp.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val timeDB = childSnapshot.key
                        val cleanedDateTime = timeDB?.replace("\"", "")
                        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val date = formatter.parse(cleanedDateTime)
                        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        val time = timeFormatter.format(date)
                        val temp = childSnapshot.child("Temperature").getValue(Int::class.java)
                        Log.i("firebase", "Latest Time: $time, Temperature: $temp")
                        textViewTemp.text = "$temp°F"
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
                        val timeDB = childSnapshot.key
                        val cleanedDateTime = timeDB?.replace("\"", "")
                        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val date = formatter.parse(cleanedDateTime)
                        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        val time = timeFormatter.format(date)
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

    private fun setupTemperatureChart() {
        lineChart.apply {
            description.text = "Temperature Over Time"
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            zoom(2f, 1f, 0f, 0f)

            xAxis.apply {
                labelCount = 5
                setLabelCount(5, true)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    private val dateFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    override fun getFormattedValue(value: Float): String {
                        return dateFormatter.format(Date(value.toLong() * 1000))
                    }
                }
                setDrawGridLines(false)
                granularity = 1f
            }
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
        }
        lineChart.setVisibleXRangeMaximum(50f)
    }
    private fun setupHumidityChart() {
        lineChartH.apply {
            description.text = "Humidity Over Time"
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            zoom(2f, 1f, 0f, 0f)

            xAxis.apply {
                labelCount = 5
                setLabelCount(5, true)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    private val dateFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    override fun getFormattedValue(value: Float): String {
                        return dateFormatter.format(Date(value.toLong() * 1000))
                    }
                }
                setDrawGridLines(false)
                granularity = 1f
            }
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
        }
        lineChartH.setVisibleXRangeMaximum(50f)
    }
    private fun listenForTemperatureUpdates() {
        dbTemp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempEntries = ArrayList<Entry>()
                snapshot.children.forEach { dataSnapshot ->
                    val timeStr = dataSnapshot.key?.replace("\"", "") ?: ""
                    val timestamp = parseDateToTimestamp(timeStr) / 1000.0f
                    val temperature = dataSnapshot.child("Temperature").getValue(Double::class.java)?.toFloat() ?: 0f
                    tempEntries.add(Entry(timestamp, temperature))
                }
                tempEntries.sortBy { it.x }

                if (tempEntries.isNotEmpty()) {
                    updateChart(tempEntries)
                } else {
                    Log.d("DataFragment", "No data available to display")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DataFragment", "Failed to read temperature", error.toException())
            }
        })
    }
    private fun listenForHumidityUpdates() {
        dbHumid.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val humidEntries = ArrayList<Entry>()
                snapshot.children.forEach { dataSnapshot ->
                    val timeStr = dataSnapshot.key?.replace("\"", "") ?: ""
                    val timestamp = parseDateToTimestamp(timeStr) / 1000.0f
                    val humidity = dataSnapshot.child("Humidity").getValue(Double::class.java)?.toFloat() ?: 0f
                    humidEntries.add(Entry(timestamp, humidity))
                }
                humidEntries.sortBy { it.x }

                if (humidEntries.isNotEmpty()) {
                    updateChartH(humidEntries)
                } else {
                    Log.d("DataFragment", "No data available to display")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("DataFragment", "Failed to read humidity", error.toException())
            }
        })
    }

    private fun updateChart(tempEntries: ArrayList<Entry>) {
        val lineDataSet = LineDataSet(tempEntries, "Temperature").apply {
            color = Color.BLUE
            lineWidth = 2.5f
            setDrawCircles(true)
            setCircleColor(Color.BLUE)
            setCircleRadius(3f)
            setDrawValues(true)
            valueTextColor = Color.BLACK
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        lineChart.data = LineData(lineDataSet)
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
    }
    private fun updateChartH(humidEntries: ArrayList<Entry>) {
        val lineDataSet = LineDataSet(humidEntries, "Humidity").apply {
            color = Color.RED
            lineWidth = 2.5f
            setDrawCircles(true)
            setCircleColor(Color.RED)
            setCircleRadius(3f)
            setDrawValues(true)
            valueTextColor = Color.BLACK
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        lineChartH.data = LineData(lineDataSet)
        lineChartH.notifyDataSetChanged()
        lineChartH.invalidate()
    }
    fun writeNewTemp( time: String, temp: Int) {
        val temp = Temperature(time, temp)
        dbTemp.setValue(temp)
    }
    fun writeNewHumid(humid: Int, time: String) {
        val humid = Humidity(time, humid)
        dbHumid.setValue(humid)
    }
    private fun parseDateToTimestamp(dateStr: String): Long {
        // Removing potential double quotes from the date string
        val cleanedDateStr = dateStr.replace("\"", "")
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            val date = formatter.parse(cleanedDateStr)
            date?.time ?: 0L
        } catch (e: ParseException) {
            Log.e("DataFragment", "Parse error for cleaned date '$cleanedDateStr'", e)
            0L
        } catch (e: Exception) {
            Log.e("DataFragment", "Unexpected error parsing cleaned date", e)
            0L
        }
    }
    fun getCurrentTime(): String {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        var minute = c.get(Calendar.MINUTE)
        var current ="$hour:$minute"
        return current
    }

}
