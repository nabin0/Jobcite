package com.nabin0.jobcite.presentation.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ForgotPasswordScreen(

) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Forgot Password Screen",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .size(25.dp)
        )
    }
}