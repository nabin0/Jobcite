package com.nabin0.jobcite.presentation.home.jobs_home.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.presentation.home.jobs_home.JobItem
import com.nabin0.jobcite.presentation.home.jobs_home.JobsScreenEvents
import com.nabin0.jobcite.presentation.home.jobs_home.JobsViewModel

@Composable
fun SearchJobsScreen(navHostController: NavHostController, viewModel: JobsViewModel) {
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.state = viewModel.state.copy(searchText = "", searchedJobsList = listOf())
                navHostController.popBackStack()
            }

        }
    }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(onBackPressedDispatcher, lifecycle) {
        onBackPressedDispatcher?.addCallback(lifecycle, backCallback)
        onDispose {
            backCallback.remove()
        }
    }


    val state = viewModel.state
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
            })

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = state.searchText,
                onValueChange = { viewModel.onEvent(JobsScreenEvents.OnSearchTextChange(it)) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                maxLines = 1, placeholder = {
                    Text(
                        text = "Search...", style = TextStyle(
                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.6f)
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.onEvent(JobsScreenEvents.SearchJobs)
                })
            )
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search Jobs",
                modifier = Modifier
                    .size(56.dp)
                    .padding(6.dp)
                    .clickable {
                        viewModel.onEvent(JobsScreenEvents.SearchJobs)
                    }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 66.dp)
        ) {
            if (state.loadingWholePage) {
                item {

                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            } else {
                if (state.searchedJobsList.isNotEmpty()) {
                    itemsIndexed(state.searchedJobsList) { _, item ->
                        JobItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            dataItem = item,
                            intentLauncher = launcher,
                            navController = navHostController
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = "No results found!",
                                style = TextStyle(fontSize = 25.sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

        }
    }


}