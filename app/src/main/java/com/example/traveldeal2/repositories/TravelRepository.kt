package com.example.traveldeal2.repositories

import  android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.enums.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insert(travel: Travel) {
        remoteDatabase.addTravel(travel)
    }

    fun update(travel: Travel) {
        remoteDatabase.updateTravel(travel)
    }

    fun getLiveData(): LiveData<Boolean> {
        return remoteDatabase.getLiveData()
    }

    fun getAllTravels(): MutableLiveData<List<Travel?>?> {
        return travelsList
    }

    fun getAllTravelsFromLocal(): LiveData<List<Travel>> {
        return localDatabase.getAllTravels()
    }

    fun getTravelsByStatus(status: List<Int>): LiveData<List<Travel>> {
        return localDatabase.getTravelsByStatus(status)
    }
}
