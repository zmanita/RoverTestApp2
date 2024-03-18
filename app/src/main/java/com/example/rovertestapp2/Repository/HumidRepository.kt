package com.example.rovertestapp2.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.rovertestapp2.Models.Temperature
import com.example.rovertestapp2.Models.humidity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HumidRepository() {
    private val dbHumid:DatabaseReference = FirebaseDatabase.getInstance().getReference("humidity")
    @Volatile private var INSTANCE : HumidRepository?= null
    private val humidTimeList = mutableListOf<humidity>()
    //private val temperatureLogDao: TemperatureLogDao

    //init {
    //    val database = AppDatabase.getDatabase(context)
    //    temperatureLogDao = database.temperatureLogDao()
    //}

    fun getInstance() : HumidRepository {

        return INSTANCE ?: synchronized(this){
            val instance = HumidRepository()
            INSTANCE = instance
            instance
        }
    }
    fun loadHumids(humidList: MutableLiveData<List<humidity>>) {
        dbHumid.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                humidTimeList.clear()
                val humid = snapshot.child("humid").getValue(Int::class.java)
                val time = snapshot.child("time").getValue(String::class.java)
                if (humid != null && time != null) {
                    humidTimeList.add(humidity(humid, time))
                }
                Log.d("loadHumids", "Posted value with list size: ${humidTimeList.size}")
                humidList.postValue(humidTimeList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("loadHumids", "Database error", error.toException())
            }
        })
    }
}
