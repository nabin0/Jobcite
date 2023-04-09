package com.nabin0.jobcite.presentation.screens

sealed class AuthScreens(val route: String) {
    object SignInScreen: AuthScreens("sign_in_screen")
    object SignUpScreen: AuthScreens("sign_up_screen")
    object VerifyEmailScreen: AuthScreens("verify_email_screen")
    object ForgotPasswordScreen: AuthScreens("forgot_password_screen")
}