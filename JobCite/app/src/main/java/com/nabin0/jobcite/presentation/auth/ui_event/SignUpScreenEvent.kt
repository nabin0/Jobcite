package com.nabin0.jobcite.presentation.auth.ui_event

sealed class SignUpScreenEvent {
    data class NameChanged(val name: String) : SignUpScreenEvent()
    data class EmailChanged(val email: String) : SignUpScreenEvent()
    data class PasswordChanged(val password: String) : SignUpScreenEvent()
    object SignUp : SignUpScreenEvent()
}