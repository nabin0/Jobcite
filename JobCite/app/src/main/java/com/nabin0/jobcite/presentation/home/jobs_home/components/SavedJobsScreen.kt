package com.nabin0.jobcite.presentation.home.saved_jobs

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.nabin0.jobcite.presentation.home.jobs_home.JobItem
import com.nabin0.jobcite.presentation.home.jobs_home.SavedJobsScreenEvents
import com.nabin0.jobcite.presentation.home.jobs_home.SavedJobsTaskOperationEvent
import com.nabin0.jobcite.presentation.home.jobs_home.SavedJobsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedJobsScreen(navHostController: NavHostController, savedJobsViewModel: SavedJobsViewModel) {

    val state = savedJobsViewModel.state
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {})

    val refreshState = rememberPullRefreshState(refreshing = state.refreshing, onRefresh = {
        savedJobsViewModel.onEvent(SavedJobsScreenEvents.refresh)
    })

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        savedJobsViewModel.uiEventChannel.collect() { event ->
            when (event) {
                is SavedJobsTaskOperationEvent.Failure -> {
                    Toast.makeText(context, event.failureMsg, Toast.LENGTH_SHORT).show()
                }
                is SavedJobsTaskOperationEvent.Success -> {
                    Toast.makeText(context, event.successMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Saved Jobs",
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            })
        },
    ) {
        Box(modifier = Modifier.pullRefresh(refreshState)) {

            LazyColumn(
                modifier = Modifier
                    .padding(bottom = it.calculateBottomPadding())
                    .fillMaxSize()
            ) {
                if (state.jobsList == null) {
                    item {
                        Text(text = "No Item Found")
                    }
                } else {
                    itemsIndexed(state.jobsList) { _, item ->
                        JobItem(modifier = Modifier.fillMaxWidth(),
                            dataItem = item,
                            intentLauncher = launcher,
                            navController = navHostController,
                            showDeleteIcon = true,
                            onDeleteClick = {
                                savedJobsViewModel.onEvent(SavedJobsScreenEvents.deleteJob(item))
                            })
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = state.refreshing, state = refreshState, modifier = Modifier.align(
                    Alignment.TopCenter
                ), contentColor = MaterialTheme.colors.onSurface
            )
        }
    }
}