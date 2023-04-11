package com.nabin0.jobcite.presentation.home.jobs_home

sealed class JobsScreenEvents {
    object GetJobs : JobsScreenEvents()
    object GetMobileDevJobs : JobsScreenEvents()
    object GetWebsiteDevJobs : JobsScreenEvents()
    object SearchJobs: JobsScreenEvents()
    data class OnSearchTextChange(val text: String): JobsScreenEvents()
    object OnSearchIconClick : JobsScreenEvents()
    object OnSearchBarCloseIconClick : JobsScreenEvents()
    object OnRefresh : JobsScreenEvents()
}