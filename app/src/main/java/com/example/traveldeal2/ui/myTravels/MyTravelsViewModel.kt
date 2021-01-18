package com.example.traveldeal2.ui.company

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.enums.Status
import com.example.traveldeal2.repositories.TravelRepository
import com.example.traveldeal2.utils.AddressTool
import com.example.traveldeal2.utils.App
import com.example.traveldeal2.utils.Utils.Companion.decodeKey
import com.example.traveldeal2.utils.Utils.Companion.encodeKey
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MyTravelsViewModel: ViewModel() {
    val app = App
    private var rp: TravelRepository = TravelRepository(app.instance)
    var travelsList: MutableLiveData<List<Travel?>?>? = MutableLiveData(listOf())
    var filteredList: MutableLiveData<List<Travel?>?>? = MutableLiveData(listOf())

    init {
        rp.getTravelsByStatus(listOf( Status.RECEIVED.ordinal, Status.RUNNING.ordinal )).observeForever {
            travelsList?.postValue(it.filter { it.company != null && it.company!!.contains(encodeKey(FirebaseAuth.getInstance().currentUser?.email))})
        }
    }

    fun updateItem(travel: Travel) {
        rp.update(travel)
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?>? {
        return travelsList
    }
}