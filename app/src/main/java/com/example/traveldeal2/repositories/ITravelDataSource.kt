package com.example.traveldeal2.repositories

import androidx.lifecycle.LiveData
import com.example.traveldeal2.data.entities.Travel

interface ITravelDataSource {
    fun addTravel(travel: Travel)
    fun updateTravel(travel: Travel)
    fun getAllTravels(): MutableList<Travel>
    fun getLiveData(): LiveData<Boolean>
    interface NotifyLiveData{
        fun onDataChange ()
    }
    fun setNotifyLiveData(obj : NotifyLiveData)
}