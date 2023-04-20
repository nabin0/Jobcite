package com.nabin0.jobcite.presentation.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.presentation.home.jobs_home.JobItem
import com.nabin0.jobcite.presentation.home.jobs_home.JobsScreenEvents
import com.nabin0.jobcite.presentation.home.jobs_home.JobsViewModel
import com.nabin0.jobcite.presentation.home.jobs_home.components.ShimmerAnimationForJobitems
import com.nabin0.jobcite.presentation.screens.Screens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JobHomeScreen(viewModel: JobsViewModel, navController: NavHostController) {

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
            })

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

    val backDropScaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)

    BackdropScaffold(
        appBar = {
            JobsScreenTopAppBar(navController)
        },
        scaffoldState = backDropScaffoldState,

        peekHeight = BackdropScaffoldDefaults.PeekHeight,
        persistentAppBar = true,
        backLayerBackgroundColor = Color(0XFF30E3DF),
        backLayerContent = {
            BackLayerContent(
                navController = navController,
                launcher = launcher
            )
        },

        frontLayerContent = {
            FrontLayerContent(
                viewModel = viewModel,
                navController = navController,
                launcher = launcher
            )
        },
        stickyFrontLayer = true,
        headerHeight = BackdropScaffoldDefaults.HeaderHeight,
        frontLayerScrimColor = Color.Unspecified,
        frontLayerShape = BackdropScaffoldDefaults.frontLayerShape,
        frontLayerElevation = BackdropScaffoldDefaults.FrontLayerElevation,
        gesturesEnabled = true,

        ) {

    }
}


@Composable
fun JobsScreenTopAppBar(
    navController: NavHostController,
) {
    TopAppBar(
        backgroundColor = Color(0XFF30E3DF),
        elevation = 0.dp,
        title = {
            Text(
                text = "Jobcite",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 20.sp
                )
            )
        }, actions = {
            IconButton(onClick = {
                navController.navigate(Screens.SearchJobScreen.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }, modifier = Modifier
            .height(56.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FrontLayerContent(
    navController: NavHostController,
    viewModel: JobsViewModel,
    launcher: ActivityResultLauncher<Intent>
) {

    var refreshing by remember {
        mutableStateOf(false)
    }
    val state = viewModel.state

    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            viewModel.onEvent(JobsScreenEvents.OnRefresh)
            refreshing = false
        })
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Divider(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.2f)
                    .align(Alignment.TopCenter)
                    .zIndex(1f).background(MaterialTheme.colors.onPrimary.copy(0.5f)),
                thickness = 5.dp
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 34.dp)
                .pullRefresh(refreshState)
        ) {
            // All Jobs Section
            item {
                if (state.loadingWholePage) {
                    ShimmerAnimationForJobitems(imageHeight = 250.dp)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        Constants.SCREEN_TITLE, "All Jobs"
                                    )
                                    navController.navigate(Screens.JobListScreen.route)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Jobs",
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = "Chevron Right",
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )

                        }
                        LazyRow(
                            modifier = Modifier
                                .padding(6.dp)
                        ) {
                            items(state.allJobsList.take(3)) { item ->
                                JobItem(
                                    modifier = Modifier.width(300.dp),
                                    dataItem = item,
                                    intentLauncher = launcher,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }

            // Mobile dev jobs section
            item {
                if (state.loadingMobileDevJobsSection) {
                    ShimmerAnimationForJobitems(imageHeight = 250.dp)
                } else {
                    if (state.mobileDevJobsList.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 8.dp, end = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    )
                                    .clickable {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            Constants.SCREEN_TITLE, "Mobile Jobs"
                                        )
                                        navController.navigate(Screens.JobListScreen.route)
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Mobile Development Jobs",
                                    style = TextStyle(
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Filled.ChevronRight,
                                    contentDescription = "Chevron Right",
                                    tint = MaterialTheme.colors.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            LazyRow(
                                modifier = Modifier
                                    .padding(6.dp)
                            ) {
                                items(state.mobileDevJobsList.take(3)) { item ->
                                    JobItem(
                                        modifier = Modifier.width(300.dp),
                                        dataItem = item,
                                        intentLauncher = launcher,
                                        navController = navController
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Web dev jobs section
            item {
                if (state.loadingWebDevJobsSection) {
                    ShimmerAnimationForJobitems(imageHeight = 250.dp)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        Constants.SCREEN_TITLE, "Website Jobs"
                                    )
                                    navController.navigate(Screens.JobListScreen.route)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Website Development Jobs",
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = "Chevron Right",
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        LazyRow(
                            modifier = Modifier
                                .padding(6.dp)
                        ) {
                            items(state.webDevJobsList.take(3)) { item ->
                                JobItem(
                                    modifier = Modifier.width(300.dp),
                                    dataItem = item,
                                    intentLauncher = launcher,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = refreshState,
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun BackLayerContent(navController: NavHostController, launcher: ActivityResultLauncher<Intent>) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp).clip(RoundedCornerShape(percent = 10))
                    .background(Color.Black.copy(0.1f))
                    .clickable {
                        navController.navigate(Screens.SavedJobs.route)
                    },
            ) {
                Column(
                    Modifier
                        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Bookmarks,
                        contentDescription = "Bookmark",
                        modifier = Modifier
                            .size(24.dp), tint = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Saved Jobs",
                        style = TextStyle(fontSize = 14.sp)
                    )

                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp).clip(RoundedCornerShape(percent = 10))
                    .background(Color.Black.copy(0.1f))
                    .clickable {
                        val intent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(Constants.GITHUB_SOURCE_CODE_LINK)
                            )
                        launcher.launch(intent)
                    }
            ) {
                Column(
                    Modifier
                        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Code,
                        contentDescription = "source code",
                        modifier = Modifier
                            .size(24.dp), tint = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Source Code",
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp).clip(RoundedCornerShape(percent = 10))
                    .background(Color.Black.copy(0.1f))
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Event,
                        contentDescription = "events",
                        modifier = Modifier
                            .size(24.dp), tint = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Events Update",
                        style = TextStyle(fontSize = 14.sp)
                    )

                }
            }
        }
    }
}
