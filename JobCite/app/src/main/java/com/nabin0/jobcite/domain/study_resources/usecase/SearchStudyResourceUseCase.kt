package com.nabin0.jobcite.domain.study_resources.usecase

import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.study_resources.StudyResourceRepository

class SearchStudyResourceUseCase(
    private val studyResourceRepository: StudyResourceRepository
) {
    suspend operator fun invoke(query: String, result: (Resource<List<StudyResourceModel>>) -> Unit) {
        studyResourceRepository.getSearchResourcesDataFromFirestore(query = query, result)
    }
}