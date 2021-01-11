package com.example.traveldeal2.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.traveldeal2.data.entities.Travel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LocalDatabase(context: Context) : ILocalDatabase {
    private val database = TravelRoomDatabase.getDatabase(context, CoroutineScope(SupervisorJob()))
    var travelDao = database.travelDao()

    fun getTravel(id: String?): LiveData<Travel?>? {
        return travelDao.getTravelById(id)
    }

    override fun addTravel(p: Travel) {
        travelDao.insert(p)
    }

    override fun addTravels(travelList: List<Travel?>?) {
        travelDao.insertList(travelList)
    }

    override fun editTravel(p: Travel) {
        travelDao.update(p)
    }

    override fun deleteTravel(p: Travel) {
        travelDao.delete(p)
    }

    override fun getAllTravels(): LiveData<List<Travel>> {
        //val l = travelDao.getTravels().observeForever { it -> Log.i("test", it.size.toString()) }
        return travelDao.getTravels()
    }

    override fun clearTable() {
        travelDao.deleteAll()
    }
}