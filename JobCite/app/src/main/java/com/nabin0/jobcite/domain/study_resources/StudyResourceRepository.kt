package com.nabin0.jobcite.domain.study_resources

import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel
import com.nabin0.jobcite.data.utils.Resource

interface StudyResourceRepository {

    suspend fun storeDataToFirestore(
        studyResourceModel: StudyResourceModel,
        result: (Resource<String>) -> Unit
    )

    suspend fun getAllResourcesDataFromFirestore(result:(Resource<List<StudyResourceModel>>) -> Unit)
    suspend fun getSearchResourcesDataFromFirestore(query: String, result:(Resource<List<StudyResourceModel>>) -> Unit)
}