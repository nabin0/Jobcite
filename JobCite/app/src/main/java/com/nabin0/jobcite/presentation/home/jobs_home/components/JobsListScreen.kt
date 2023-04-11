package com.nabin0.jobcite.presentation.home.jobs_home.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.presentation.home.components.LoadingListShimmer
import com.nabin0.jobcite.presentation.home.jobs_home.JobItem
import com.nabin0.jobcite.presentation.home.jobs_home.JobsScreenEvents
import com.nabin0.jobcite.presentation.home.jobs_home.JobsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JobsListScreen(
    viewModel: JobsViewModel,
    navController: NavHostController,
    title: String? = "All Jobs"
) {
    val state = viewModel.state
    val listState = rememberLazyListState()
    var lastIndex by remember {
        mutableStateOf(-1)
    }

    var refreshing by remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
            })

    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            viewModel.onEvent(JobsScreenEvents.OnRefresh)
            refreshing = false
        })
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.uiEventChannel.collect() { event ->
            when (event) {
                is JobsViewModel.JobsUiEvent.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
                JobsViewModel.JobsUiEvent.Success -> {
                    Toast.makeText(context, "Task Successful.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = refreshState)
    ) {
        LazyColumn(
            state = listState
        ) {
            item {
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Text(text = title.toString(), style = TextStyle(fontSize = 25.sp))
                }

                Spacer(modifier = Modifier.height(5.dp))
            }


            if (state.loadingWholePage) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        LoadingListShimmer(imageHeight = 300.dp, padding = 8.dp)
                    }
                }
            } else {
                if (state.allJobsList.isEmpty()) {
                    item {
                        Text(
                            text = "No items found",
                            modifier = Modifier.fillParentMaxSize(),
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {

                    val list = when (title) {
                        "All Jobs" -> state.allJobsList
                        "Website Jobs" -> state.webDevJobsList
                        "Mobile Jobs" -> state.mobileDevJobsList
                        else -> state.allJobsList
                    }

                    itemsIndexed(list) { index, item ->
                        val offsetX = animateFloatAsState(
                            if (index > lastIndex) -600f else 0f,
                            animationSpec = tween(durationMillis = 300)
                        )
                        val lastVisibleIndex =
                            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                        lastIndex = lastVisibleIndex - 1

                        JobItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = offsetX.value.dp),
                            dataItem = item,
                            intentLauncher = launcher,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}