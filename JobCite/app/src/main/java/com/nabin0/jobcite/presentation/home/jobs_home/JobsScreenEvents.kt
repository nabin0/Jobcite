package com.nabin0.jobcite.presentation.home.jobs_home

sealed class JobsScreenEvents {
    object getJobs : JobsScreenEvents()
    object searchJobs: JobsScreenEvents()
    data class onSearchTextChange(val text: String): JobsScreenEvents()
    object onSearchIconClick : JobsScreenEvents()
    object onSearchBarCloseIconClick : JobsScreenEvents()
    object onRefresh : JobsScreenEvents()
}