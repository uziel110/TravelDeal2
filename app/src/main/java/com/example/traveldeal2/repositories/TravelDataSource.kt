package com.example.traveldeal2.repositories

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.traveldeal2.data.entities.Travel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * fire base com.example.traveldeal.data source
 */
class TravelDataSource :
    ITravelDataSource {
    private val rootNode = FirebaseDatabase.getInstance()
    private val reference = rootNode.getReference("travels")
    private val liveData: MutableLiveData<Boolean> = MutableLiveData()
    private var uid: String = FirebaseAuth.getInstance().uid.toString()
    var allTravelsList: MutableList<Travel> = mutableListOf()

    lateinit var notifyData: ITravelDataSource.NotifyLiveData

    init {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allTravelsList.clear()
                for (travelSnapshot in dataSnapshot.children) {
                    val travel: Travel? = travelSnapshot.getValue(Travel::class.java)
                    if (travel != null) {
                        allTravelsList.add(travel)

                    }
                }
                // travels.value = travelsList
                notifyData.onDataChange()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    override fun addTravel(travel: Travel) {
        val curKey = reference.child(uid).push().key
        if (curKey == null) {
            Log.w(TAG, "Couldn't get push key for travels")
            return
        }
        travel.clientId = curKey
        reference.child(uid).child(curKey).setValue(travel).addOnSuccessListener {
            liveData.value = true
        }.addOnFailureListener {
            liveData.value = false
        }
    }

    override fun updateTravel(travel: Travel) {
        val curKey = travel.travelId
        reference.child(curKey).setValue(travel)
    }


    override fun getLiveData(): LiveData<Boolean> {
        return liveData
    }

    override fun getAllTravels(): MutableList<Travel> {
        return allTravelsList
    }

    override fun setNotifyLiveData(obj: ITravelDataSource.NotifyLiveData) {
        notifyData = obj
    }
}