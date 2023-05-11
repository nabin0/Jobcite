package com.nabin0.jobcite.domain.auth.usecase

data class AuthUseCases(
    val validateEmailUseCase: ValidateEmailUseCase,
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val validateNameUseCase: ValidateNameUseCase,
    val signInUseCase: SignInUseCase,
    val signOutUseCase: SignOutUseCase,
    val signUpUseCase: SignUpUseCase,
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val sendEmailVerification: SendEmailVerificationUseCase,
    val resetPasswordUseCase: ResetScreenUseCase,
)