package com.example.rovertestapp2.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rovertestapp2.Repository.HumidRepository
import com.example.rovertestapp2.Repository.TempRepository

class HumidViewModel : ViewModel(){
    private val repository : HumidRepository = HumidRepository().getInstance()
    private val _allHumids = MutableLiveData<List<humidity>>()
    val allHumids : LiveData<List<humidity>> = _allHumids

    init {
        repository.loadHumids(_allHumids)
    }


}