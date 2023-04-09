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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.presentation.auth.ui_event.SignUpScreenEvent
import com.nabin0.jobcite.presentation.auth.viewmodel.AuthViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    navigateToSignInScreen: () -> Unit,
    navigateToVerifyEmailScreen: () -> Unit
) {
    val state = viewModel.signUpState
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = context) {
        viewModel.signUpEventChannel.collect { event ->
            when (event) {
                is AuthViewModel.SignUpValidationEvents.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
                AuthViewModel.SignUpValidationEvents.Success -> {
                    navigateToVerifyEmailScreen()
                    Toast.makeText(
                        context,
                        "Account created successfully. Verify your account.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


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
            text = "Create Account",
            fontSize = 50.sp,
            color = Color(0XFF30E3DF),
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            value = state.name,
            onValueChange = { viewModel.onSignUpEvent(SignUpScreenEvent.NameChanged(name = it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            textStyle = MaterialTheme.typography.body1,
            label = {
                Text(text = "Name")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            )
        )
        if (state.nameError != null) {
            Text(
                text = state.nameError,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .align(Alignment.Start),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = state.email,
            onValueChange = { viewModel.onSignUpEvent(SignUpScreenEvent.EmailChanged(email = it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            textStyle = MaterialTheme.typography.body1,
            label = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            )
        )
        if (state.emailError != null) {
            Text(
                text = state.emailError,
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
            onValueChange = { viewModel.onSignUpEvent(SignUpScreenEvent.PasswordChanged(password = it)) },
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
        if (state.passwordError != null) {
            Text(
                text = state.passwordError,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .align(Alignment.Start),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.onSignUpEvent(SignUpScreenEvent.SignUp)
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
                .height(50.dp).padding(top = 2.dp, bottom = 2.dp)
                .background( Color(0XFF30E3DF))
        ) {
            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            } else {
                Text(text = "SIGNUP")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                append("Already have a account? ")
            }
            withStyle(style = SpanStyle(color = Color(0XFF30E3DF))) {
                append("Login")
            }
        }, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .clickable {
                navigateToSignInScreen()
            })
    }
}