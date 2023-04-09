package com.nabin0.jobcite.domain.auth.usecase

import com.nabin0.jobcite.domain.auth.uitls.AuthFormValidationResult

class ValidateNameUseCase {
    operator fun invoke(name: String): AuthFormValidationResult {
        if (name.length < 2) {
            return AuthFormValidationResult(
                successful = false, errorMessage = "Enter a valid name."
            )
        }
        return AuthFormValidationResult(
            successful = true, errorMessage = null
        )
    }
}