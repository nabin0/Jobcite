package com.nabin0.jobcite.domain.auth.usecase

import com.google.firebase.auth.FirebaseUser
import com.nabin0.jobcite.domain.auth.repository.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): FirebaseUser? {
        return authRepository.currentUser
    }
}