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

    private var email: String? = Utils.encodeKey(FirebaseAuth.getInstance().currentUser?.email)
    private var uidMapTravels = hashMapOf<Travel, String>()

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
        checkNewChanged()
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

    private fun checkNewChanged() {
        val runnable = Runnable {
            //some code here
            for (travel in travelsList.value!!)
                if (travel != null) {
                    if (travel.company.filter { it.value }.keys.first() == email)
                        if (uidMapTravels[travel] != email) {
                            //brodCast test
                            uidMapTravels[travel] = email!!
                            Utils.sendBroadcastCustomIntent("הנסיעה אושרה")
                        }
                }
        }
//        Thread(runnable).start()
    }
}
