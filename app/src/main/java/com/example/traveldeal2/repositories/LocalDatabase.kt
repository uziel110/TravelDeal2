package com.example.traveldeal2.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.traveldeal2.data.entities.Travel

class LocalDatabase(context: Context) : ILocalDatabase {

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getLocalDatabase(context: Context): LocalDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = LocalDatabase(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private val database = TravelRoomDatabase.getDatabase(context)
    private var travelDao = database.travelDao()

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
        return travelDao.getTravels()
    }

    fun getTravelsByStatus(status: List<Int>): LiveData<List<Travel>> {
        return travelDao.getTravelsByStatus(status)
    }

    override fun clearTable() {
        travelDao.deleteAll()
    }
}