package com.nabin0.jobcite.presentation.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.auth.usecase.AuthUseCases
import com.nabin0.jobcite.presentation.auth.ui_event.SignInScreenEvent
import com.nabin0.jobcite.presentation.auth.ui_event.SignUpScreenEvent
import com.nabin0.jobcite.presentation.auth.ui_state.SignInScreenState
import com.nabin0.jobcite.presentation.auth.ui_state.SignUpScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    // --- SignIn ---

    private val _signInEventChannel = Channel<SignInValidationEvents>()
    val signInEventChannel = _signInEventChannel.receiveAsFlow()

    var signInState by mutableStateOf(SignInScreenState())

    fun onSignInEvent(event: SignInScreenEvent) {
        when (event) {
            is SignInScreenEvent.EmailChanged -> {
                signInState = signInState.copy(email = event.email)
            }
            is SignInScreenEvent.PasswordChanged -> {
                signInState = signInState.copy(password = event.password)
            }
            SignInScreenEvent.SignIn -> {
                submitSignInData()
            }
        }
    }

    private fun submitSignInData() {

        val emailResult = authUseCases.validateEmailUseCase(signInState.email)
        val passwordResult = authUseCases.validatePasswordUseCase(signInState.password)

        val hasError = listOf(
            emailResult, passwordResult
        ).any { !it.successful }

        if (hasError) {
            signInState = signInState.copy(
                emailErrorMessage = emailResult.errorMessage,
                passwordErrorMessage = passwordResult.errorMessage
            )
            return
        }
        signInState = signInState.copy(
            emailErrorMessage = null, passwordErrorMessage = null
        )
        viewModelScope.launch {
            signInState = signInState.copy(loading = true)
            when (val signInResult = authUseCases.signInUseCase(signInState.email, signInState.password)) {
                is Resource.Failure -> {
                    signInState = signInState.copy(loading = false)

                    when (signInResult.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            _signInEventChannel.send(SignInValidationEvents.Failure("The account do not exist. Create an account first to login."))
                        }
                        else -> {
                            _signInEventChannel.send(SignInValidationEvents.Failure(signInResult.exception.toString()))
                        }
                    }
                }
                Resource.Loading -> {
                    signInState = signInState.copy(loading = true)
                }
                is Resource.Success -> {
                    signInState = signInState.copy(loading = false)
                    _signInEventChannel.send(SignInValidationEvents.Success)
                }
            }
        }
    }

    // --- SignUp ---
    private val _signUpEventChannel = Channel<SignUpValidationEvents>()
    val signUpEventChannel = _signUpEventChannel.receiveAsFlow()

    var signUpState by mutableStateOf(SignUpScreenState())

    fun onSignUpEvent(event: SignUpScreenEvent) {
        when (event) {
            is SignUpScreenEvent.EmailChanged -> {
                signUpState = signUpState.copy(email = event.email)
            }
            is SignUpScreenEvent.NameChanged -> {
                signUpState = signUpState.copy(name = event.name)
            }
            is SignUpScreenEvent.PasswordChanged -> {
                signUpState = signUpState.copy(password = event.password)
            }
            SignUpScreenEvent.SignUp -> {
                submitSignUpData()
            }
        }
    }

    private fun submitSignUpData() {
        val nameResult = authUseCases.validateNameUseCase(signUpState.name)
        val emailResult = authUseCases.validateEmailUseCase(signUpState.email)
        val passwordResult = authUseCases.validatePasswordUseCase(signUpState.password)

        val hasError = listOf(
            nameResult, emailResult, passwordResult
        ).any { !it.successful }

        if (hasError) {
            signUpState = signUpState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                nameError = nameResult.errorMessage
            )
            return
        }
        signUpState = signUpState.copy(
            emailError = null, passwordError = null, nameError = null
        )
        viewModelScope.launch {
            signUpState = signUpState.copy(loading = true)
            val signUpResult = authUseCases.signUpUseCase(
                signUpState.name, signUpState.email, signUpState.password
            )
            when (signUpResult) {
                is Resource.Failure -> {
                    signUpState = signUpState.copy(loading = false)

                    when (signUpResult.exception) {
                        is FirebaseAuthWeakPasswordException -> {
                            _signUpEventChannel.send(SignUpValidationEvents.Failure("Password is too weak. Please select a storng password."))
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            _signUpEventChannel.send(SignUpValidationEvents.Failure("Invalid Credentials. Check your email and password and try again."))
                        }
                        is FirebaseAuthUserCollisionException -> {
                            _signUpEventChannel.send(SignUpValidationEvents.Failure("An account already exist with this email."))
                        }
                        else -> {
                            _signUpEventChannel.send(SignUpValidationEvents.Failure(signUpResult.exception.toString()))
                        }
                    }

                }
                Resource.Loading -> {
                    signUpState = signUpState.copy(loading = true)
                }
                is Resource.Success -> {
                    signUpState = signUpState.copy(loading = false)
                    launch {
                        val res = authUseCases.sendEmailVerification()
                        when (res) {
                            is Resource.Failure -> _signUpEventChannel.send(
                                SignUpValidationEvents.Failure(
                                    res.exception.toString()
                                )
                            )
                            Resource.Loading -> {}
                            is Resource.Success -> {}
                        }

                    }
                    _signUpEventChannel.send(SignUpValidationEvents.Success)
                }
            }
        }
    }


    /**
     *  ViewModel to ui interaction events
     */

    sealed class SignInValidationEvents {
        object Success : SignInValidationEvents()
        data class Failure(val errorMessage: String) : SignInValidationEvents()
    }

    sealed class SignUpValidationEvents {
        object Success : SignUpValidationEvents()
        data class Failure(val errorMessage: String) : SignUpValidationEvents()
    }

}