package com.example.traveldeal2.repositories

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class TravelRepository(context: Context) : Application() {

    private var remoteDatabase: ITravelDataSource = TravelDataSource()
    private val localDatabase = LocalDatabase.getLocalDatabase(context)
    val travelsList = MutableLiveData<List<Travel?>?>()

    init {
        val notifyData: ITravelDataSource.NotifyLiveData =
            object : ITravelDataSource.NotifyLiveData {
                override fun onDataChange() {
                    val tempList = remoteDatabase.getAllTravels()
                    travelsList.postValue(tempList)
                    localDatabase.clearTable()
                    localDatabase.addTravels(tempList)
                }
            }
        remoteDatabase.setNotifyLiveData(notifyData)
    }

    fun update(travel: Travel) {
        remoteDatabase.updateTravel(travel)
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?> {
        return travelsList
    }

    fun getTravelsByStatus(status: List<Int>): LiveData<List<Travel>> {
        return localDatabase.getTravelsByStatus(status)
    }
}
