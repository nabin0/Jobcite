package com.nabin0.jobcite.presentation.home.jobs_home.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nabin0.jobcite.presentation.home.jobs_home.JobItem
import com.nabin0.jobcite.presentation.home.jobs_home.JobsScreenEvents
import com.nabin0.jobcite.presentation.home.jobs_home.JobsViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchJobsScreen(navHostController: NavHostController, viewModel: JobsViewModel) {

    var showSuggestionList by remember {
        mutableStateOf(false)
    }
    var searchPerformed by remember {
        mutableStateOf(false)
    }
    val state = viewModel.state
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
            })

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    val suggestedWords =
        listOf("Android", "Website", "Software", "Engineer", "Developer", "Kotlin", "Java")


    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.state = viewModel.state.copy(searchText = "", searchedJobsList = listOf())
                searchPerformed = false
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


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(percent = 50))

            ) {
                TextField(
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                showSuggestionList = true
                                searchPerformed = false
                            }
                        }.padding(6.dp),
                    value = state.searchText,
                    onValueChange = { viewModel.onEvent(JobsScreenEvents.OnSearchTextChange(it)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    maxLines = 1,
                    placeholder = {
                        Text(
                            text = "Search...", style = TextStyle(
                                color = MaterialTheme.colors.onPrimary.copy(alpha = 0.6f)
                            )
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onPrimary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        showSuggestionList = false
                        keyboardController?.hide()
                        viewModel.onEvent(JobsScreenEvents.SearchJobs)
                        focusManager.clearFocus(force = true)
                        searchPerformed = true
                    }),
                )
            }

            // Suggest words
            if (state.searchText.isNotEmpty()) {
                val filteredSuggestionList = suggestedWords.filter {
                    it.contains(state.searchText, ignoreCase = true)
                }
                if (filteredSuggestionList.isNotEmpty()) {
                    if (showSuggestionList) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                        ) {
                            items(filteredSuggestionList) { item ->
                                Text(
                                    text = item,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.onEvent(
                                                JobsScreenEvents.OnSearchTextChange(
                                                    item
                                                )
                                            )
                                        }
                                        .padding(16.dp)
                                )
                                Divider(thickness = 0.5.dp)
                            }
                        }
                    }
                }

            }

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.loadingWholePage) {
                    item {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        ) {
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
                    } else if (searchPerformed) {
                        item {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center)
                            ) {
                                Text(text = "No Jobs Found!!!", textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }

    }


}