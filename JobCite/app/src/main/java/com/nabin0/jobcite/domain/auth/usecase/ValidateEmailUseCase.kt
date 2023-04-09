package com.nabin0.jobcite.domain.auth.usecase

import android.util.Patterns
import com.nabin0.jobcite.domain.auth.uitls.AuthFormValidationResult

class ValidateEmailUseCase {
    operator fun invoke(email: String): AuthFormValidationResult {
        if (email.isBlank()) {
            return AuthFormValidationResult(
                successful = false,
                errorMessage = "Email cannot be empty."
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthFormValidationResult(
                successful = false,
                errorMessage = "Please enter a valid email."
            )
        }

        return AuthFormValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}