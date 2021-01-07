package com.example.traveldeal2.data.repositories

import com.example.traveldeal2.data.entities.Travel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

/**
 * fire base data source
 */
class TravelDataSource {
    private val rootNode = FirebaseDatabase.getInstance()
    private val reference = rootNode.getReference("travels")

    fun insert(travel: Travel): Task<Void> {
        return reference.push().setValue(travel)
    }

    //val query: Query = eMailRef.orderByChild("RequestStatus").equalTo("נשלח")

    fun getTravelsByEMail(eMail: String): List<Travel> {
        val list: MutableList<Travel> = mutableListOf()
        val eMailRef = reference.child(eMail)
        eMailRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (snapshot in dataSnapshot.children)
                    list.add(snapshot.value as Travel)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return list
    }
}