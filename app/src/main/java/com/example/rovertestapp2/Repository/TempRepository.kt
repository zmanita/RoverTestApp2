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
    private val dbTemp:DatabaseReference = FirebaseDatabase.getInstance().getReference("Temperature")
    @Volatile private var INSTANCE : TempRepository?= null
    private val tempTimeList = mutableListOf<Temperature>()

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
                snapshot.children.forEach { timeSnapshot ->
                    val time = timeSnapshot.key ?: return@forEach
                    val temp = timeSnapshot.child("Temperature").getValue(Int::class.java) ?: return@forEach
                    tempTimeList.add(Temperature(time, temp))
                }
                tempTimeList.reverse()
                Log.d("loadTemps", "Posted value with list size: ${tempTimeList.size}")
                tempList.postValue(tempTimeList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("loadTemps", "Database error", error.toException())
            }
        })
    }
    fun deleteAllTemperatureValues(tempList: MutableLiveData<List<Temperature>>) {
        dbTemp.setValue("").addOnSuccessListener {
            Log.i("Firebase", "Successfully deleted all temperature values")
            tempTimeList.clear()
            tempList.postValue(tempTimeList)
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Error deleting temperature values", exception)
        }
    }

}
