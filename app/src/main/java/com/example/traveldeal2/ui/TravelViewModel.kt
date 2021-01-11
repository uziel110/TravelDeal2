package com.example.traveldeal2.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.data.repositories.TravelRepository

class TravelViewModel(app: Application) : AndroidViewModel(app) {
    private var rp: TravelRepository = TravelRepository(app)
    //var travels = MutableLiveData<List<Travel>>()

    fun insertItem(travel: Travel) {
        rp.insert(travel)
    }

    fun getLiveData(): LiveData<Boolean> {
        return rp.getLiveData()
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?> {
        return rp.getAllTravels()
    }

    fun getClosedTravels(): MutableLiveData<List<Travel?>?> {
        val closedTravelsList: MutableList<Travel> = mutableListOf()
        val v = rp.getAllTravels().value
        for (travel in v!!)
            if (travel!!.requestStatus == "הסתיים")
                closedTravelsList.add(travel)
        return MutableLiveData(closedTravelsList)
    }

//    fun getTravel(id: String): MutableLiveData<Travel> {
//        return rp.getTravel(id)
//    }
}