package com.nabin0.jobcite.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.nabin0.jobcite.R
import com.nabin0.jobcite.presentation.auth.viewmodel.AuthViewModel

@Composable
fun VerifyEmailScreen(
    viewModel: AuthViewModel?,
    navigateToSignInScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                    append("An email with verification link is send.\n\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.primary2),
                        fontSize = 24.sp,
                    )
                ) {
                    append("Already Verified?\n\n")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                    append("If not, please verify. Do not forget to check the spam folder if email is not found.")
                }
            }, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navigateToSignInScreen()
                })
    }
}
