package com.example.traveldeal2.ui.admin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.repositories.TravelRepository
import com.example.traveldeal2.utils.App

class AdminViewModel : ViewModel(), IAdminViewModel {
    val app = App
    private var rp: TravelRepository = TravelRepository(app.instance)

    override fun insertItem(travel: Travel) {
        rp.insert(travel)
    }

    override fun getLiveData(): LiveData<Boolean> {
        return rp.getLiveData()
    }

    override fun getAllTravels(): MutableLiveData<List<Travel?>?> {
        return rp.getAllTravels()
    }

    override fun getTravelsByStatus(string: String): MutableLiveData<List<Travel?>?> {
        val travels: MutableList<Travel> = mutableListOf()
        val allTravels = getAllTravels().value
        if (allTravels != null) {
            for (travel in allTravels)
                if (travel!!.requestStatus == string) travels.add(travel)
        }
        return MutableLiveData(travels)
    }
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is slideshow Fragment"
//    }
//    val text: LiveData<String> = _text
}