package com.nabin0.jobcite.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabin0.jobcite.R
import com.nabin0.jobcite.presentation.auth.ui_event.SignInScreenEvent
import com.nabin0.jobcite.presentation.auth.viewmodel.AuthViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    navigateToForgotScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit
) {

    val state = viewModel.signInState
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.signInEventChannel.collect { event ->
            when (event) {
                is AuthViewModel.SignInValidationEvents.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
                AuthViewModel.SignInValidationEvents.Success -> {
                    navigateToHomeScreen()
                    Toast.makeText(context, "Successful login", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .verticalScroll(rememberScrollState())
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                fontSize = 50.sp,
                color =  colorResource(id = R.color.primary2),
                style = MaterialTheme.typography.h2
            )
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                value = state.email,
                onValueChange = { viewModel.onSignInEvent(SignInScreenEvent.EmailChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                textStyle = MaterialTheme.typography.body1,
                label = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                isError = state.emailErrorMessage != null
            )
            if (state.emailErrorMessage != null) {
                Text(
                    text = state.emailErrorMessage,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.Start),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = state.password,
                onValueChange = { viewModel.onSignInEvent(SignInScreenEvent.PasswordChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                textStyle = MaterialTheme.typography.body1,
                label = {
                    Text(text = "Password")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
            )
            if (state.passwordErrorMessage != null) {
                Text(
                    text = state.passwordErrorMessage,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.Start),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Forgot Password?", modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        navigateToForgotScreen()
                    }, style = TextStyle(
                    color = MaterialTheme.colors.onPrimary
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = {
                    viewModel.onSignInEvent(SignInScreenEvent.SignIn)
                },
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .height(50.dp).padding(top = 2.dp, bottom = 2.dp),
                enabled = !state.loading
            ) {
                if (state.loading) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                } else {
                    Text(text = "LOGIN")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                    append("Don't have account? ")
                }
                withStyle(style = SpanStyle(color =  colorResource(id = R.color.primary2))) {
                    append("create a new account.")
                }
            }, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navigateToSignUpScreen()
                })
        }
    }

}