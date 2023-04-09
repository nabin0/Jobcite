package com.nabin0.jobcite.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.nabin0.jobcite.Constants.SHARED_PREF_NAME
import com.nabin0.jobcite.data.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPrefModule {

    @Singleton
    @Provides
    fun provideSharedPref(context: Application): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREF_NAME, Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideSharedPreferenceManager(sharedPreferences: SharedPreferences): PreferenceManager {
        return PreferenceManager(sharedPreferences = sharedPreferences)
    }
}