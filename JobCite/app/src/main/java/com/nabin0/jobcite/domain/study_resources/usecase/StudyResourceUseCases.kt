package com.nabin0.jobcite.domain.study_resources.usecase

data class StudyResourceUseCases(
    val getStudyResourceUseCases: GetStudyResourcesUseCase,
    val storeStudyResourceUseCase: StoreResourceToFirebaseUseCase,
    val searchStudyResourceUseCase: SearchStudyResourceUseCase
)