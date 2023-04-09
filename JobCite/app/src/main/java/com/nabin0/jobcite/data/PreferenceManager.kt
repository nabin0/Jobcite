package com.nabin0.jobcite.data

import android.content.SharedPreferences

class PreferenceManager(private val sharedPreferences: SharedPreferences) {

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun getSharedPref(): SharedPreferences = sharedPreferences
}