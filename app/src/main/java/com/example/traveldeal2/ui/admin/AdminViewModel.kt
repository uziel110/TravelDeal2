package com.example.traveldeal2.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.enums.Status
import com.example.traveldeal2.repositories.TravelRepository
import com.example.traveldeal2.utils.App

class AdminViewModel : ViewModel() {
    val app = App
    private var rp: TravelRepository = TravelRepository(app.instance)
    var travelsList: MutableLiveData<List<Travel?>?>? = MutableLiveData(listOf())

    init {
        rp.getAllTravelsFromLocal().observeForever {
            travelsList?.postValue(it?.filter { travel ->
                travel!!.requestStatus == Status.CLOSED
            })
        }
    }

    fun insertItem(travel: Travel) {
        rp.insert(travel)
    }

    fun getLiveData(): LiveData<Boolean> {
        return rp.getLiveData()
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?> {
        return rp.getAllTravels()
    }

//    fun getTravelsByStatus(string: String): MutableLiveData<List<Travel?>?> {
//        val travels: MutableList<Travel> = mutableListOf()
//        val allTravels = getAllTravels().value
//        if (allTravels != null) {
//            for (travel in allTravels)
//                if (travel!!.requestStatus == string) travels.add(travel)
//        }
//        return MutableLiveData(travels)
//    }
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is slideshow Fragment"
//    }
//    val text: LiveData<String> = _text
}