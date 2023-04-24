package com.nabin0.jobcite.data

import com.google.firebase.firestore.FirebaseFirestore
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.data.utils.Resource

interface EventsRepository{
    suspend fun getEventsFromFirebase(callback: (Resource<List<String>>) -> Unit)
}

class EventsRepositoryImpl(
    private val firestore: FirebaseFirestore
): EventsRepository{
    override suspend fun getEventsFromFirebase(callback: (Resource<List<String>>) -> Unit) {
        firestore.collection(Constants.EVENTS).get().addOnSuccessListener { snapshot ->
           val eventsList = mutableListOf<String>()
            for(document in snapshot){
                val strData = document.getString("data")
                strData?.let{
                    eventsList.add(it)
                }
            }
            callback.invoke(Resource.Success(eventsList))
        }.addOnFailureListener{
            callback.invoke(Resource.Failure(it))
        }
    }
}