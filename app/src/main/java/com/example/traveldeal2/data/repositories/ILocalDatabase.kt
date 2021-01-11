package com.example.traveldeal2.data.repositories

import androidx.lifecycle.LiveData
import com.example.traveldeal2.data.entities.Travel

interface ILocalDatabase {
    fun addTravel(p: Travel)
    fun addTravels(travelList: List<Travel?>?)
    fun editTravel(p: Travel)
    fun deleteTravel(p: Travel)
    fun getAllTravels(): LiveData<List<Travel>>
    fun clearTable()
}