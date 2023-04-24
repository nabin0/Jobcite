package com.nabin0.jobcite.presentation.screens

sealed class Screens(val route: String) {
    object StudyResourceDetailScreen: Screens("study_resource_detail_screen")
    object JobDetailScreen: Screens("job_detail_screen")
    object SavedJobs: Screens("saved_jobs_screen")
    object JobListScreen: Screens("job_list_Screen")
    object SearchJobScreen: Screens("search_job_screen")
    object AboutAppScreen: Screens("about_app_screen")
    object EventsScreen: Screens("events_screen")
}