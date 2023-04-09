package com.nabin0.jobcite.domain.auth.usecase

import com.nabin0.jobcite.domain.auth.repository.AuthRepository

class SignOutUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(){
        return authRepository.signOut()
    }
}