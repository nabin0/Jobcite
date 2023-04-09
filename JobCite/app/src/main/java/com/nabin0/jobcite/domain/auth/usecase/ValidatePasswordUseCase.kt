package com.nabin0.jobcite.domain.auth.usecase

import com.nabin0.jobcite.domain.auth.uitls.AuthFormValidationResult

class ValidatePasswordUseCase {
    operator fun invoke(password: String): AuthFormValidationResult {
        if (password.length < 6) {
            return AuthFormValidationResult(
                successful = false, errorMessage = "Password should be at least 6 char long."
            )
        }

        return AuthFormValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}