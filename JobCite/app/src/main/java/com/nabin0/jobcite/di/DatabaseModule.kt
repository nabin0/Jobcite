package com.nabin0.jobcite.di

import android.app.Application
import androidx.room.Room
import com.nabin0.jobcite.data.jobs.db.JobDatabase
import com.nabin0.jobcite.data.jobs.db.JobsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {


    @Singleton
    @Provides
    fun providesJobsDatabase(context: Application): JobDatabase {
        return Room.databaseBuilder(context.applicationContext, JobDatabase::class.java, "jobs_db")
            .build()
    }


    @Singleton
    @Provides
    fun providesJobsDao(jobDatabase: JobDatabase): JobsDao {
        return jobDatabase.jobDao()
    }


}