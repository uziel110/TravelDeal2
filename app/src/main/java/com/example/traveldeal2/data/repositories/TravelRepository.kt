package com.example.traveldeal2.data.repositories

import  android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.traveldeal2.data.entities.Travel

class TravelRepository(application: Application) : Application() {

    private var remoteDatabase: ITravelDataSource = TravelDataSource()
    private val localDatabase = LocalDatabase(application.applicationContext)
    val travelsList = MutableLiveData<List<Travel?>?>()
    init {
        val notifyData : ITravelDataSource.NotifyLiveData = object : ITravelDataSource.NotifyLiveData{
            override fun onDataChange() {
                var tempList = remoteDatabase.getAllTravels()
                travelsList.postValue(tempList)
                localDatabase.clearTable()
                localDatabase.addTravels(tempList)
            }
        }
        remoteDatabase.setNotifyLiveData(notifyData)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insert(travel: Travel) {
        remoteDatabase.addTravel(travel)
    }

    fun getLiveData(): LiveData<Boolean> {
        return remoteDatabase.getLiveData()
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?> {
        return travelsList
    }
//
//    fun getTravel(id: String): MutableLiveData<Travel> {
//        return remoteDatabase.getTravel(id)
//    }
}
