package com.nabin0.jobcite.domain.study_resources.usecase

import com.google.firebase.firestore.FirebaseFirestore
import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.data.utils.await
import com.nabin0.jobcite.domain.study_resources.StudyResourceRepository

class StoreResourceToFirebaseUseCase(
    private val studyResourceRepository: StudyResourceRepository
) {
    suspend operator fun invoke(
        studyResourceModel: StudyResourceModel,
        result: (Resource<String>) -> Unit
    ) {
        return studyResourceRepository.storeDataToFirestore(studyResourceModel, result)
    }
}