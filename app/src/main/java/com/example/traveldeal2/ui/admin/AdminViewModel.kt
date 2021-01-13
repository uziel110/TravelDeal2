package com.example.traveldeal2.ui.admin

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.repositories.TravelRepository
import com.example.traveldeal2.utils.App
import java.util.*

class AdminViewModel : ViewModel() {
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
    fun getTravelsByDate(_startDate: String, _endDate: String): MutableLiveData<List<Travel?>?>? {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val startDate: Date = sdf.parse(_startDate)
        val endDate: Date = sdf.parse(_endDate)
        filteredList?.postValue(travelsList?.value?.filter { travel ->
            val trStartDate: Date = sdf.parse(travel?.returnDate)
            val trEndDate: Date = sdf.parse(travel?.returnDate)
            trStartDate.compareTo(startDate) >= 0
                    && trEndDate.compareTo(endDate) <= 0
        })
        return filteredList
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?>? {
        return travelsList
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