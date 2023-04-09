package com.nabin0.jobcite.domain.auth.usecase

import com.google.firebase.auth.FirebaseUser
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.auth.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Resource<FirebaseUser?> {
       return authRepository.signUp(name, email, password)
    }
}