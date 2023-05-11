package com.nabin0.jobcite.di

import com.nabin0.jobcite.domain.auth.repository.AuthRepository
import com.nabin0.jobcite.domain.auth.usecase.*
import com.nabin0.jobcite.domain.study_resources.StudyResourceRepository
import com.nabin0.jobcite.domain.study_resources.usecase.GetStudyResourcesUseCase
import com.nabin0.jobcite.domain.study_resources.usecase.SearchStudyResourceUseCase
import com.nabin0.jobcite.domain.study_resources.usecase.StoreResourceToFirebaseUseCase
import com.nabin0.jobcite.domain.study_resources.usecase.StudyResourceUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UseCaseModule {

    @Singleton
    @Provides
    fun provideAuthUseCases(authRepository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            validateEmailUseCase = ValidateEmailUseCase(),
            validatePasswordUseCase = ValidatePasswordUseCase(),
            validateNameUseCase = ValidateNameUseCase(),
            signInUseCase = SignInUseCase(authRepository),
            signOutUseCase = SignOutUseCase(authRepository),
            signUpUseCase = SignUpUseCase(authRepository),
            getCurrentUserUseCase = GetCurrentUserUseCase(authRepository),
            sendEmailVerification = SendEmailVerificationUseCase(authRepository),
            resetPasswordUseCase = ResetScreenUseCase(authRepository)
        )
    }

    @Singleton
    @Provides
    fun provideStudyResourceDataUseCases(repository: StudyResourceRepository): StudyResourceUseCases {
        return StudyResourceUseCases(
            getStudyResourceUseCases = GetStudyResourcesUseCase(repository),
            storeStudyResourceUseCase = StoreResourceToFirebaseUseCase(repository),
            searchStudyResourceUseCase = SearchStudyResourceUseCase(repository)
        )
    }


}