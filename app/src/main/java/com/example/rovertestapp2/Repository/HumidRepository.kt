package com.example.rovertestapp2.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rovertestapp2.Models.Humidity
import com.example.rovertestapp2.Models.Temperature
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HumidRepository() {
    private val dbHumid:DatabaseReference = FirebaseDatabase.getInstance().getReference("Humidity")
    @Volatile private var INSTANCE : HumidRepository?= null
    private val humidTimeList = mutableListOf<Humidity>()

    fun getInstance() : HumidRepository {

        return INSTANCE ?: synchronized(this){
            val instance = HumidRepository()
            INSTANCE = instance
            instance
        }
    }
    fun loadHumids(humidList: MutableLiveData<List<Humidity>>) {
        dbHumid.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { timeSnapshot ->
                    val time = timeSnapshot.key ?: return@forEach
                    val humid = timeSnapshot.child("Humidity").getValue(Int::class.java) ?: return@forEach
                    humidTimeList.add(Humidity(time, humid))
                }
                humidTimeList.reverse()
                Log.d("loadHumids", "Posted value with list size: ${humidTimeList.size}")
                humidList.postValue(humidTimeList)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("loadHumids", "Database error", error.toException())
            }
        })
    }
    fun deleteAllHumidityValues(humidList: MutableLiveData<List<Humidity>>) {
        dbHumid.setValue("").addOnSuccessListener {
            Log.i("Firebase", "Successfully deleted all humidity values")
            humidTimeList.clear()
            humidList.postValue(humidTimeList)
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Error deleting humidity values", exception)
        }
    }
}
