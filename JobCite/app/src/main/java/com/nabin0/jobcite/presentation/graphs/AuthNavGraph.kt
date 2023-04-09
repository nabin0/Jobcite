package com.nabin0.jobcite.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.nabin0.jobcite.presentation.auth.*
import com.nabin0.jobcite.presentation.auth.viewmodel.AuthViewModel
import com.nabin0.jobcite.presentation.graphs.Graph
import com.nabin0.jobcite.presentation.screens.AuthScreens

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController, authViewModel: AuthViewModel
) {
    navigation(
        route = Graph.AUTHENTICATION, startDestination = AuthScreens.SignInScreen.route
    ) {
        composable(
            route = AuthScreens.SignInScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            SignInScreen(viewModel = authViewModel, navigateToForgotScreen = {
                navController.navigate(AuthScreens.ForgotPasswordScreen.route)
            }, navigateToSignUpScreen = {
                navController.navigate(AuthScreens.SignUpScreen.route)
            },
                navigateToHomeScreen = {
                    navController.navigate(Graph.HOME) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                })
        }

        composable(
            route = AuthScreens.ForgotPasswordScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            ForgotPasswordScreen()
        }

        composable(
            route = AuthScreens.SignUpScreen.route,
                    enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            SignUpScreen(
                viewModel = authViewModel,
                navigateToSignInScreen = {
                    navController.navigate(AuthScreens.SignInScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                navigateToVerifyEmailScreen = {
                    navController.navigate(AuthScreens.VerifyEmailScreen.route)
                },
            )
        }

        composable(
            route = AuthScreens.VerifyEmailScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }

        ) {
            VerifyEmailScreen(viewModel = authViewModel, navigateToSignInScreen = {
                navController.navigate(AuthScreens.SignInScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            })
        }

    }
}