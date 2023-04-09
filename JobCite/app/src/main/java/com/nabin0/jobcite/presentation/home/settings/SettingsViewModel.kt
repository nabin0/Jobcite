package com.nabin0.jobcite.presentation.home.settings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nabin0.jobcite.App
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.data.PreferenceManager
import com.nabin0.jobcite.domain.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager,
): ViewModel() {

    var state by mutableStateOf(SettingScreenState())

    init {
        val isDarkModeEnabled = preferenceManager.getBoolean(Constants.DARK_MODE)
        state = state.copy(isDarkModeOn = isDarkModeEnabled)

        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            currentUser.displayName?.let {
                state = state.copy(userName = it)
            }
            currentUser.email?.let {
                state = state.copy(userEmail = it)
            }
        }
    }

    fun onEvent(events: SettingScreenEvents) {
        when (events) {
            SettingScreenEvents.SignOut -> {
                authRepository.signOut()
                preferenceManager.clearAll()
            }
            is SettingScreenEvents.OnDarkModeSwitchToggle -> {
                state = state.copy(isDarkModeOn = events.isSwitchOn)
                preferenceManager.putBoolean(Constants.DARK_MODE, events.isSwitchOn)
            }
        }
    }
}


sealed class SettingScreenEvents {
    object SignOut : SettingScreenEvents()
    data class OnDarkModeSwitchToggle(val isSwitchOn: Boolean) : SettingScreenEvents()
}

data class SettingScreenState(
    val userName: String = "",
    val userEmail: String = "",
    val isDarkModeOn: Boolean = false,
)