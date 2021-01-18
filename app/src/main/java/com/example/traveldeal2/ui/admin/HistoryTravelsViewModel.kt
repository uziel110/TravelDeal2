package com.example.traveldeal2.ui.admin

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.enums.Status
import com.example.traveldeal2.repositories.TravelRepository
import com.example.traveldeal2.utils.App
import java.util.*

class HistoryTravelsViewModel : ViewModel() {
    val app = App
    private var rp: TravelRepository = TravelRepository(app.instance)
    var travelsList: MutableLiveData<List<Travel?>?>? = MutableLiveData(listOf())
    var filteredList: MutableLiveData<List<Travel?>?>? = MutableLiveData(listOf())

    init {
        rp.getTravelsByStatus(listOf(Status.CLOSED.ordinal, Status.PAID.ordinal)).observeForever {
            travelsList?.postValue(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTravelsByDate(_startDate: String, _endDate: String): MutableLiveData<List<Travel?>?> {
        val t = Thread {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val startDate: Date = sdf.parse(_startDate)
            val endDate: Date = sdf.parse(_endDate)
            filteredList?.postValue(travelsList?.value?.filter { travel ->
                val retDate: Date = sdf.parse(travel?.returnDate)
                retDate in startDate..endDate// && travel?.requestStatus?.equals(Status.CLOSED)!!
            })
        }
        t.start()
        return filteredList!!
    }

    fun getClosedTravels(): MutableLiveData<List<Travel?>?>? {
        return travelsList!!
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?>? {
        rp.getAllTravels().observeForever {
            travelsList?.postValue(it)
        }
        return travelsList
    }

    fun updateItem(travel: Travel) {
        rp.update(travel)
    }
}