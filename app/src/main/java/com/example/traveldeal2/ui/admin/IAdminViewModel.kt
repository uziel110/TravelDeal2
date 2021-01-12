package com.example.traveldeal2.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.traveldeal2.data.entities.Travel

interface IAdminViewModel {
    fun insertItem(travel: Travel)

    fun getLiveData(): LiveData<Boolean>

    fun getAllTravels(): MutableLiveData<List<Travel?>?>

    fun getTravelsByStatus(string: String): MutableLiveData<List<Travel?>?>
}