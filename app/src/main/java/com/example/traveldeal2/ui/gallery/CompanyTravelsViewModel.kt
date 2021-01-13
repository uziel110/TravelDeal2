package com.example.traveldeal2.ui.gallery

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.repositories.TravelRepository
import com.example.traveldeal2.utils.AddressTool
import com.example.traveldeal2.utils.App
import com.google.android.gms.maps.model.LatLng
import java.util.*

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

    fun insertItem(travel: Travel) {
        rp.insert(travel)
    }

    fun getLiveData(): LiveData<Boolean> {
        return rp.getLiveData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTravelsByDate(_startDate: String, _endDate: String): MutableLiveData<List<Travel?>?> {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val startDate: Date = sdf.parse(_startDate)
        val endDate: Date = sdf.parse(_endDate)
        filteredList?.postValue(travelsList?.value?.filter { travel ->
            val retDate: Date = sdf.parse(travel?.returnDate)
            retDate in startDate..endDate
        })
        return filteredList!!
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