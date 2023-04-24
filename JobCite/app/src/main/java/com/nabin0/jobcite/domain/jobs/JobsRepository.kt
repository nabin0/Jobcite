package com.nabin0.jobcite.domain.jobs

import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import com.nabin0.jobcite.data.utils.Resource

interface JobsRepository {
    suspend fun getJobs(result:(Resource<List<JobsModelItem>?>) -> Unit)
    suspend fun searchJobs(query: String, result:(Resource<List<JobsModelItem>?>) -> Unit)
    suspend fun getSavedJobs(result:(Resource<List<JobsModelItem>?>) -> Unit)
    suspend fun hasJobItem(jobItem: JobsModelItem): Boolean
    suspend fun deleteSavedJob(jobItem: JobsModelItem, result: (Resource<String?>) -> Unit)
    suspend fun saveJob(jobItem: JobsModelItem, result: (Resource<String?>) -> Unit)
}