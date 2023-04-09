package com.nabin0.jobcite.presentation.auth.ui_state

data class SignUpScreenState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val loading: Boolean = false
)