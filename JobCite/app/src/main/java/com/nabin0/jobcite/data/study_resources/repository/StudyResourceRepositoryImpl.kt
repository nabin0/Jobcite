package com.nabin0.jobcite.data.study_resources.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.study_resources.StudyResourceRepository
import java.lang.Exception

class StudyResourceRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
) : StudyResourceRepository {
    override suspend fun storeDataToFirestore(
        studyResourceModel: StudyResourceModel, result: (Resource<String>) -> Unit
    ) {
        firebaseFirestore.collection(Constants.COLLECTION_FIREBASE_STUDY_RESOURCE)
            .add(studyResourceModel).addOnSuccessListener {
                result.invoke(
                    Resource.Success("Added Successfully.")
                )
            }.addOnFailureListener { exception ->
                result.invoke(
                    Resource.Failure(exception)
                )
            }
    }

    override suspend fun getAllResourcesDataFromFirestore(result: (Resource<List<StudyResourceModel>>) -> Unit) {
        firebaseFirestore.collection(Constants.COLLECTION_FIREBASE_STUDY_RESOURCE).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val resourceData: MutableList<StudyResourceModel> = mutableListOf()
                    for (documentSnapshot: DocumentSnapshot in task.result) {
                        val resourceModel: StudyResourceModel? =
                            documentSnapshot.toObject(StudyResourceModel::class.java)
                        if (resourceModel != null) {
                            resourceData.add(resourceModel)
                        }
                    }
                    result.invoke(
                        Resource.Success(
                            resourceData
                        )
                    )
                } else {
                    result.invoke(
                        Resource.Failure(
                            Exception(task.exception)
                        )
                    )
                }
            }
    }

    override suspend fun getSearchResourcesDataFromFirestore(
        query: String, result: (Resource<List<StudyResourceModel>>) -> Unit
    ) {
        firebaseFirestore.collection(Constants.COLLECTION_FIREBASE_STUDY_RESOURCE)
            .whereArrayContains("tags", query).get().addOnSuccessListener { snapshot ->
                val resourceData: MutableList<StudyResourceModel> = mutableListOf()
                for (document in snapshot) {
                    val resourceModel: StudyResourceModel =
                        document.toObject(StudyResourceModel::class.java)
                    resourceData.add(resourceModel)
                }
                result.invoke(Resource.Success(resourceData))
            }.addOnFailureListener { e ->
                result.invoke(
                    Resource.Failure(
                        e
                    )
                )
            }
    }

}