package com.nabin0.jobcite.presentation.home.components

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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nabin0.jobcite.presentation.home.study_resources.StudyResourceScreenEvents
import com.nabin0.jobcite.presentation.home.study_resources.StudyResourceViewModel

@Composable
fun ResourcesScreen(
    viewModel: StudyResourceViewModel, navController: NavHostController
) {
    val state = viewModel.state
    var lastIndex by remember { mutableStateOf(-1) }
    val listState = rememberLazyListState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.uiEventChannel.collect() { event ->
            when (event) {
                is StudyResourceViewModel.StudyResourceUiEvents.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is StudyResourceViewModel.StudyResourceUiEvents.Success -> {
                    Toast.makeText(context, event.successMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { viewModel.onEvent(StudyResourceScreenEvents.GetStudyResources) }) {
        LazyColumn(
            state = listState
        ) {
            item {
                ResourcesScreenAppBarWithSearchWidget(title = "Study Resources",
                    searchTextValue = state.searchText,
                    isSearchBarVisible = state.isSearchBarVisible,
                    onSearchIconClick = { viewModel.onEvent(StudyResourceScreenEvents.OnSearchIconClick) },
                    onCloseIconClick = { viewModel.onEvent(StudyResourceScreenEvents.OnSearchBarCloseIconClick) },
                    onSearchValueChange = {
                        viewModel.onEvent(
                            StudyResourceScreenEvents.OnSearchTextChanged(
                                it
                            )
                        )
                    },
                    onSearch = { viewModel.onEvent(StudyResourceScreenEvents.OnSearch) })
            }
            if (state.loading) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        LoadingListShimmer(imageHeight = 200.dp, padding = 8.dp)
                    }
                }
            } else {
                itemsIndexed(state.dataList) { index, item ->
                    val offsetX = animateFloatAsState(
                        if (index > lastIndex) -600f else 0f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val lastVisibleIndex =
                        listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                    lastIndex = lastVisibleIndex - 1

                    StudyResourceItem(
                        dataItem = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = offsetX.value.dp),
                        intentLauncher = launcher,
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }




}