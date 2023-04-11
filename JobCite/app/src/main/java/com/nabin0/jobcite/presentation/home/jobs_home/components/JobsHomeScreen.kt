package com.nabin0.jobcite.presentation.home

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.data.jobs.model.JobBasicInfo
import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import com.nabin0.jobcite.presentation.home.jobs_home.JobItem
import com.nabin0.jobcite.presentation.home.jobs_home.JobsScreenEvents
import com.nabin0.jobcite.presentation.home.jobs_home.JobsViewModel
import com.nabin0.jobcite.presentation.home.jobs_home.components.ShimmerAnimationForJobitems
import com.nabin0.jobcite.presentation.screens.Screens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JobHomeScreen(viewModel: JobsViewModel, navController: NavHostController) {

    var refreshing by remember {
        mutableStateOf(false)
    }

    val state = viewModel.state

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

    val jobItem = JobsModelItem(
        idForRoom = 1,
        companyName = "Google",
        experienceRequired = "0 - 1 years",
        hiringCompanyLink = "https://www.google.com",
        id = 2,
        jobBasicInfo = JobBasicInfo(
            industry = "it industry",
            jobFunction = "work in andorid ",
            qualification = "Btech graduate",
            role = "Senior android developer",
            specialization = "kotlin"
        ),
        jobDescription = "This is the andorid app development job opening for the skilled andoird developer haiving experience of 0 to 5 years. those who have required qualification cna join to the company.",
        jobPostedOn = "today",
        jobPostLink = "https://www.google.com",
        jobSkills = listOf("kotlin", "java", "jetpack", "compose"),
        jobTitle = "Android developer in jetpack compose",
        location = "Gurgaon",
        salary = "20lpa",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(title = {
            Text(
                text = "Jobcite",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 18.sp
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
            .padding(bottom = 5.dp)
            .height(56.dp)
            .align(Alignment.TopCenter)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp)
                .pullRefresh(refreshState)
        ) {
            // Saved and events card section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(6.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(16.dp)
                            .height(110.dp)
                            .clickable {
                                navController.navigate(Screens.SavedJobs.route)
                            }, elevation = 8.dp
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Bookmarks,
                                contentDescription = "Bookmark",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(40.dp), tint = Color(0XFF30E3DF)
                            )
                            Text(
                                text = "Saved Jobs",
                                style = TextStyle(fontSize = 20.sp)
                            )

                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(16.dp)
                            .height(110.dp)
                            .clickable {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GITHUB_SOURCE_CODE_LINK))
                                launcher.launch(intent)
                            }, elevation = 8.dp
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Code,
                                contentDescription = "source code",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp), tint = Color(0XFF30E3DF)
                            )
                            Text(
                                text = "Source Code",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(16.dp)
                            .height(110.dp), elevation = 8.dp
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Event,
                                contentDescription = "events",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp), tint = Color(0XFF30E3DF)
                            )
                            Text(
                                text = "Events Update",
                                style = TextStyle(fontSize = 20.sp)
                            )

                        }
                    }
                }
            }

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
                                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
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
                                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
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
                                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
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
