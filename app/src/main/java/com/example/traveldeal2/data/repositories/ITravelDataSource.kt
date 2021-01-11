package com.example.traveldeal2.data.repositories

import androidx.lifecycle.LiveData
import com.example.traveldeal2.data.entities.Travel

interface ITravelDataSource {
    fun addTravel(p: Travel)
    //fun addTravels(travelList: List<Travel>)
    fun editTravel(p: Travel)
    fun getAllTravels(): MutableList<Travel>
    fun getLiveData(): LiveData<Boolean>

    interface NotifyLiveData{
        fun onDataChange ()
    }
    fun setNotifyLiveData(obj : NotifyLiveData)
}