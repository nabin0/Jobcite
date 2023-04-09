package com.nabin0.jobcite.presentation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.nabin0.jobcite.presentation.authNavGraph
import com.nabin0.jobcite.presentation.auth.viewmodel.AuthViewModel
import com.nabin0.jobcite.presentation.screens.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(
    navHostController: NavHostController, userSignInStatus: Boolean,
    toggleTheme: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()

    var startDestination by remember { mutableStateOf(Graph.AUTHENTICATION) }
    startDestination = if (userSignInStatus) Graph.HOME else Graph.AUTHENTICATION

    AnimatedNavHost(
        navController = navHostController, route = Graph.ROOT, startDestination = startDestination
    ) {
        authNavGraph(navController = navHostController, authViewModel = authViewModel)
        composable(route = Graph.HOME) {
            HomeScreen(toggleTheme = toggleTheme)
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "authentication_graph"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}