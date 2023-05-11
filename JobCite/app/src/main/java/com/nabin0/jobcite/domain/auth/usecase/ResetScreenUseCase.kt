package com.nabin0.jobcite.domain.auth.usecase

import com.google.firebase.auth.FirebaseUser
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.auth.repository.AuthRepository

class ResetScreenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) {
        return authRepository.resetPassword(email)
    }
}