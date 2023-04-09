package com.nabin0.jobcite.domain.auth.uitls

data class AuthFormValidationResult(
    val successful: Boolean = false,
    val errorMessage: String? = null
)
