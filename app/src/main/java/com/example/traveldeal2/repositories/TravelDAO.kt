package com.example.traveldeal2.repositories

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.traveldeal2.data.entities.Travel

@Dao
interface TravelDAO {

    @Insert
    fun insert(travel: Travel)

    @Insert
    fun insertList(travel: List<Travel?>?)

    @Update
    fun update(travel: Travel)

    @Delete
    fun delete(travel: Travel): Unit

    @Query("SELECT * from travels_table WHERE clientId = :key")
    fun getTravelById(key: String?): LiveData<Travel?>?

    @Query("SELECT * FROM travels_table")
    fun getTravels(): LiveData<List<Travel>>

    @Query("DELETE FROM travels_table")
    fun deleteAll()
}
