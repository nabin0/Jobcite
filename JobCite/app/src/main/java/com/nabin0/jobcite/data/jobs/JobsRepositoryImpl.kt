package com.nabin0.jobcite.data.jobs

import android.util.Log
import com.nabin0.jobcite.data.jobs.db.JobsDao
import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import com.nabin0.jobcite.data.jobs.network.JobsService
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.jobs.JobsRepository

class JobsRepositoryImpl(
    private val jobsService: JobsService, private val jobsDao: JobsDao
) : JobsRepository {
    override suspend fun getJobs(result: (Resource<List<JobsModelItem>?>) -> Unit) {
        try {
            Log.d("MyTag", "job repository")
            val response = jobsService.getJobs()
            val body = response.body()
            Log.d("MyTag", "body repository")
            result.invoke(Resource.Success(body))
        } catch (e: Exception) {
            result.invoke(Resource.Failure(e))
        }
    }

    override suspend fun searchJobs(
        query: String, result: (Resource<List<JobsModelItem>?>) -> Unit
    ) {
        try {
            val response = jobsService.search(query)
            val body = response.body()
            result.invoke(Resource.Success(body))
        } catch (e: Exception) {
            result.invoke(Resource.Failure(e))
        }
    }

    override suspend fun getSavedJobs(result: (Resource<List<JobsModelItem>?>) -> Unit) {
        try {
            jobsDao.getAllJobs().let { jobsList ->
                result.invoke(
                    Resource.Success(
                        jobsList
                    )
                )
            }
        } catch (e: Exception) {
            result.invoke(Resource.Failure(e))
        }
    }

    override suspend fun hasJobItem(jobItem: JobsModelItem): Boolean {
        val result = jobsDao.hasItem(jobItem.jobPostLink)
        return result >= 1
    }

    override suspend fun deleteSavedJob(
        jobItem: JobsModelItem,
        result: (Resource<String?>) -> Unit
    ) {
        try {
            jobsDao.deleteJob(jobItem).let {
                if (it > 0) {
                    result.invoke(Resource.Success("Item deleted successfully"))
                } else {
                    result.invoke(Resource.Failure(java.lang.Exception("Failed to delete item.")))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveJob(jobItem: JobsModelItem, result: (Resource<String?>) -> Unit) {
        try {
            jobsDao.saveJob(jobItem).let {
                if (it > 0) {
                    result.invoke(Resource.Success("Item saved successfully"))
                } else {
                    result.invoke(Resource.Failure(java.lang.Exception("Failed to save item.")))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}