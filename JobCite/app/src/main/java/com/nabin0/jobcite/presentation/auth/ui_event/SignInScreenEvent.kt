package com.nabin0.jobcite.presentation.auth.ui_event

sealed class SignInScreenEvent {
    data class EmailChanged(val email: String) : SignInScreenEvent()
    data class PasswordChanged(val password: String) : SignInScreenEvent()
    object SignIn : SignInScreenEvent()
}