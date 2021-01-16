package com.example.traveldeal2.ui.company

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.repositories.TravelRepository
import com.example.traveldeal2.utils.AddressTool
import com.example.traveldeal2.utils.App
import com.google.android.gms.maps.model.LatLng

class CompanyTravelsViewModel : ViewModel() {
    val app = App
    private var rp: TravelRepository = TravelRepository(app.instance)
    var travelsList: MutableLiveData<List<Travel?>?>? = MutableLiveData(listOf())
    var filteredList: MutableLiveData<List<Travel?>?>? = MutableLiveData(listOf())

    init {
        rp.getAllTravelsFromLocal().observeForever {
            travelsList?.postValue(it)
        }
    }
//
//    fun insertItem(travel: Travel) {
//        rp.insert(travel)
//    }


    fun updateItem(travel: Travel) {
        rp.update(travel)
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?>? {
        return travelsList
    }

    fun getRelevantTravels(
        radius: Int,
        location: LatLng,
        context: Context
    ): MutableLiveData<List<Travel?>?> {
        val t = Thread {
            filteredList?.postValue(travelsList?.value?.filter { it ->
                location.let { it1 ->
                    AddressTool.getLocationFromAddress(
                        context, it!!.departureAddress
                    )?.let { it2 ->
                        AddressTool.calculateDistance(it1, it2)
                    }
                }!! <= radius
            })
        }
        t.start()
        return filteredList!!
    }
}