package com.nabin0.jobcite.presentation

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.nabin0.jobcite.App
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.Constants.LOGIN_STATUS
import com.nabin0.jobcite.Constants.TAG
import com.nabin0.jobcite.data.PreferenceManager
import com.nabin0.jobcite.presentation.graphs.RootNavigationGraph
import com.nabin0.jobcite.ui.theme.JobCiteTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isDarkThemeEnabled = mutableStateOf(preferenceManager.getBoolean(Constants.DARK_MODE))

        setContent {
            if (isDarkThemeEnabled.value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.statusBarColor = Color.Black.toArgb()
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.statusBarColor = Color.White.toArgb()
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }

            JobCiteTheme(darkTheme = isDarkThemeEnabled.value) {
                isDarkThemeEnabled.value = preferenceManager.getBoolean(Constants.DARK_MODE)
                val navController = rememberAnimatedNavController()
                val userSignInStatus = preferenceManager.getBoolean(LOGIN_STATUS)
                RootNavigationGraph(
                    navHostController = navController,
                    userSignInStatus,
                    toggleTheme = {
                        isDarkThemeEnabled.value = !isDarkThemeEnabled.value
                    })
            }

        }
    }
}