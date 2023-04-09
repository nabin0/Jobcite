package com.nabin0.jobcite.presentation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import com.nabin0.jobcite.data.study_resources.model.StudyResourceModel
import com.nabin0.jobcite.presentation.home.JobHomeScreen
import com.nabin0.jobcite.presentation.home.components.DiscussScreen
import com.nabin0.jobcite.presentation.home.components.ResourcesScreen
import com.nabin0.jobcite.presentation.home.disscuss.DiscussScreenViewModel
import com.nabin0.jobcite.presentation.home.jobs_home.JobsViewModel
import com.nabin0.jobcite.presentation.home.jobs_home.SavedJobsViewModel
import com.nabin0.jobcite.presentation.home.jobs_home.components.JobDetailScreen
import com.nabin0.jobcite.presentation.home.saved_jobs.SavedJobsScreen
import com.nabin0.jobcite.presentation.home.settings.SettingsScreen
import com.nabin0.jobcite.presentation.home.settings.SettingsViewModel
import com.nabin0.jobcite.presentation.home.study_resources.StudyResourceViewModel
import com.nabin0.jobcite.presentation.home.study_resources.components.StudyResourceDetailScreen
import com.nabin0.jobcite.presentation.screens.BottomBarScreens
import com.nabin0.jobcite.presentation.screens.Screens

@Composable
fun HomeNavGraph(
    modifier: Modifier, navHostController: NavHostController,
    toggleTheme: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        route = Graph.HOME,
        startDestination = BottomBarScreens.Home.route
    ) {
        composable(route = BottomBarScreens.Home.route) {
            val jobsViewModel: JobsViewModel = hiltViewModel()
            JobHomeScreen(viewModel = jobsViewModel, navController = navHostController)
        }
        composable(route = BottomBarScreens.Discuss.route) {
            val discussViewModel: DiscussScreenViewModel = hiltViewModel()
            DiscussScreen(viewModel = discussViewModel)
        }
        composable(route = BottomBarScreens.Resources.route) {
            val resourcesViewModel: StudyResourceViewModel = hiltViewModel()
            ResourcesScreen(navController = navHostController, viewModel = resourcesViewModel)
        }

        composable(route = BottomBarScreens.Settings.route) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(navHostController = navHostController, viewModel = settingsViewModel, toggleTheme = toggleTheme)
        }

        composable(
            route = Screens.StudyResourceDetailScreen.route
        ) {
            val item =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<StudyResourceModel>(
                    Constants.STUDY_RESOURCE_ITEM
                )
            StudyResourceDetailScreen(dataItem = item, navHostController = navHostController)
        }

        composable(
            route = Screens.JobDetailScreen.route
        ) {
            val item =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<JobsModelItem>(
                    Constants.JOB_DATA_ITEM
                )
            val savedJobsViewModel: SavedJobsViewModel = hiltViewModel()
            JobDetailScreen(
                dataItem = item,
                navHostController = navHostController,
                savedJobsViewModel = savedJobsViewModel
            )
        }

        composable(route = Screens.SavedJobs.route) {
            val savedJobsViewModel: SavedJobsViewModel = hiltViewModel()
            SavedJobsScreen(
                navHostController = navHostController, savedJobsViewModel = savedJobsViewModel
            )
        }
    }
}