package com.nabin0.jobcite.presentation.home.jobs_home

import com.nabin0.jobcite.data.jobs.model.JobsModelItem

data class JobsScreenState(
    val allJobsList: List<JobsModelItem> = listOf(),
    val mobileDevJobsList: List<JobsModelItem> = listOf(),
    val webDevJobsList: List<JobsModelItem> = listOf(),
    val searchedJobsList: List<JobsModelItem> = listOf(),
    val loadingWholePage : Boolean = false,
    val loadingMobileDevJobsSection : Boolean = false,
    val loadingWebDevJobsSection : Boolean = false,
    val isSearchBarVisible: Boolean = false,
    val searchText: String = ""
)