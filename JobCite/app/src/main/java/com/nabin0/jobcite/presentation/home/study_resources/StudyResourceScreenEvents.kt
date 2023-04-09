package com.nabin0.jobcite.presentation.home.study_resources

sealed class StudyResourceScreenEvents {
    object GetStudyResources : StudyResourceScreenEvents()
    data class OnSearchTextChanged(val searchText: String) : StudyResourceScreenEvents()
    object OnSearch : StudyResourceScreenEvents()
    object OnSearchIconClick : StudyResourceScreenEvents()
    object OnSearchBarCloseIconClick : StudyResourceScreenEvents()
}