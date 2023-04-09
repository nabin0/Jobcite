package com.nabin0.jobcite.data.jobs.network

import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JobsService {

    @GET("jobs")
    suspend fun getJobs(): Response<List<JobsModelItem>>

    @GET("search")
    suspend fun search(
        @Query("query") query: String
    ): Response<List<JobsModelItem>>

    companion object {
        const val BASE_URL = "http://nabin0.pythonanywhere.com/"
    }
}