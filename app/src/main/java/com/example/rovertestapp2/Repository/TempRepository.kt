package com.example.rovertestapp2.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.rovertestapp2.Models.Temperature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TempRepository() {
    private val dbTemp:DatabaseReference = FirebaseDatabase.getInstance().getReference("temperature")
    @Volatile private var INSTANCE : TempRepository?= null
    private val tempTimeList = mutableListOf<Temperature>()
    //private val temperatureLogDao: TemperatureLogDao

    //init {
    //    val database = AppDatabase.getDatabase(context)
    //    temperatureLogDao = database.temperatureLogDao()
    //}

    fun getInstance() : TempRepository {

        return INSTANCE ?: synchronized(this){
            val instance = TempRepository()
            INSTANCE = instance
            instance
        }
    }
    fun loadTemps(tempList: MutableLiveData<List<Temperature>>) {
        dbTemp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempTimeList.clear()
                val temp = snapshot.child("temp").getValue(Int::class.java)
                val time = snapshot.child("time").getValue(String::class.java)
                if (temp != null && time != null) {
                    tempTimeList.add(Temperature(temp, time))
                }
                Log.d("loadTemps", "Posted value with list size: ${tempTimeList.size}")
                tempList.postValue(tempTimeList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("loadTemps", "Database error", error.toException())
            }
        })
    }
}
