package com.nabin0.jobcite.data.jobs.db

import androidx.room.*
import com.nabin0.jobcite.data.jobs.model.JobsModelItem

@Dao
interface JobsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(jobItem: JobsModelItem): Long

    @Query("SELECT * FROM jobs")
    suspend fun getAllJobs(): List<JobsModelItem>

    @Delete
    suspend fun deleteJob(jobItem: JobsModelItem): Int
}