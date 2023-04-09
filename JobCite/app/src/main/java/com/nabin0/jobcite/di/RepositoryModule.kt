package com.nabin0.jobcite.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nabin0.jobcite.data.PreferenceManager
import com.nabin0.jobcite.domain.auth.repository.AuthRepository
import com.nabin0.jobcite.data.authentication.AuthRepositoryImpl
import com.nabin0.jobcite.data.chat.ChatRepositoryImpl
import com.nabin0.jobcite.data.jobs.JobsRepositoryImpl
import com.nabin0.jobcite.data.jobs.db.JobsDao
import com.nabin0.jobcite.data.jobs.network.JobsService
import com.nabin0.jobcite.data.study_resources.repository.StudyResourceRepositoryImpl
import com.nabin0.jobcite.domain.chat.ChatRepository
import com.nabin0.jobcite.domain.jobs.JobsRepository
import com.nabin0.jobcite.domain.study_resources.StudyResourceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth, preferenceManager: PreferenceManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            firebaseAuth = firebaseAuth, preferenceManager = preferenceManager
        )
    }

    @Singleton
    @Provides
    fun provideStudyDataResourceRepository(firebaseFirestore: FirebaseFirestore): StudyResourceRepository {
        return StudyResourceRepositoryImpl(firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideJobsRepository(jobsService: JobsService, jobsDao: JobsDao): JobsRepository {
        return JobsRepositoryImpl(jobsService, jobsDao)
    }

    @Singleton
    @Provides
    fun provideChatRepository(firebaseFirestore: FirebaseFirestore): ChatRepository {
        return ChatRepositoryImpl(firebaseFirestore)
    }

}