package com.nabin0.jobcite.presentation.home.study_resources

import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel

data class StudyResourceState(
    val dataList: List<StudyResourceModel> = listOf(), val loading: Boolean = false,
    val isSearchBarVisible: Boolean = false,
    val searchText: String = ""
)