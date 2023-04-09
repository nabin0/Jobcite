package com.nabin0.jobcite.presentation.auth.ui_state

data class SignInScreenState(
    val email: String = "",
    val emailErrorMessage: String? = null,
    val password: String = "",
    val passwordErrorMessage: String? = null,
    val loading: Boolean = false
)
