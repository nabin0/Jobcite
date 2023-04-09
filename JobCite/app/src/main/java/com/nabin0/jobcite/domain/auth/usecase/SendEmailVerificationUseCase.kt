package com.nabin0.jobcite.domain.auth.usecase

import com.google.firebase.auth.FirebaseUser
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.auth.repository.AuthRepository

class SendEmailVerificationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Boolean> {
        return authRepository.sendEmailVerification()
    }
}