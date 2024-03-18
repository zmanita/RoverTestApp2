package com.example.rovertestapp2.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rovertestapp2.Repository.TempRepository

class TempViewModel : ViewModel(){
    private val repository : TempRepository = TempRepository().getInstance()
    private val _allTemps = MutableLiveData<List<Temperature>>()
    val allTemps : LiveData<List<Temperature>> = _allTemps

    init {
        repository.loadTemps(_allTemps)
    }


}