package com.nabin0.jobcite.presentation.home.jobs_home

import com.nabin0.jobcite.data.jobs.model.JobsModelItem

data class JobsScreenState(
    val jobsList: List<JobsModelItem> = listOf(),
    val loading : Boolean = false,
    val isSearchBarVisible: Boolean = false,
    val searchText: String = ""
)