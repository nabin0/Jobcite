package com.nabin0.jobcite.presentation.home.jobs_home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.jobs.JobsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobsViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel() {

    private val _uiEventChannel = Channel<JobsUiEvent>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()

    var state by mutableStateOf(JobsScreenState())

    init {
        state = state.copy(loadingWholePage = true)
        getJobs()
        getMobileDevJobs()
        getWebDevJobs()
    }

    fun onEvent(event: JobsScreenEvents) {
        when (event) {
            JobsScreenEvents.GetJobs -> {
                getJobs()
            }
            JobsScreenEvents.SearchJobs -> {
                searchJobs()
            }
            is JobsScreenEvents.OnSearchTextChange -> {
                state = state.copy(searchText = event.text)
            }
            JobsScreenEvents.OnRefresh -> {
                refresh()
            }
            JobsScreenEvents.GetMobileDevJobs -> {
                getMobileDevJobs()
            }
            JobsScreenEvents.GetWebsiteDevJobs -> {
                getWebDevJobs()
            }
        }
    }

    private fun getWebDevJobs() {
        viewModelScope.launch {
            state = state.copy(loadingWebDevJobsSection = true)
            jobsRepository.searchJobs("web") { res ->
                viewModelScope.launch {
                    when (res) {
                        is Resource.Failure -> {
                            state = state.copy(loadingWebDevJobsSection = false)
                            _uiEventChannel.send(JobsUiEvent.Failure("Error: ${res.exception}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loadingWebDevJobsSection = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loadingWebDevJobsSection = false)
                            res.result?.let {
                                state = state.copy(webDevJobsList = it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getMobileDevJobs() {
        viewModelScope.launch {
            state = state.copy(loadingMobileDevJobsSection = true)
            jobsRepository.searchJobs("app") { res ->
                viewModelScope.launch {
                    when (res) {
                        is Resource.Failure -> {
                            state = state.copy(loadingMobileDevJobsSection = false)
                            _uiEventChannel.send(JobsUiEvent.Failure("Error: ${res.exception}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loadingMobileDevJobsSection = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loadingMobileDevJobsSection = false)
                            res.result?.let {
                                state = state.copy(mobileDevJobsList = it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun refresh() {
        getJobs()
        getMobileDevJobs()
        getWebDevJobs()
    }

    private fun searchJobs() {
        viewModelScope.launch {
            state = state.copy(loadingWholePage = true)
            jobsRepository.searchJobs(state.searchText) { res ->
                viewModelScope.launch {
                    when (res) {
                        is Resource.Failure -> {
                            state = state.copy(loadingWholePage = false)
                            _uiEventChannel.send(JobsUiEvent.Failure("Error: ${res.exception}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loadingWholePage = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loadingWholePage = false)
                            res.result?.let {
                                state = state.copy(searchedJobsList = it)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getJobs() {
        viewModelScope.launch {
            state = state.copy(loadingWholePage = true)
            jobsRepository.getJobs { res ->
                viewModelScope.launch {
                    when (res) {
                        is Resource.Failure -> {
                            state = state.copy(loadingWholePage = false)
                            _uiEventChannel.send(JobsUiEvent.Failure("Error: ${res.exception}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loadingWholePage = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loadingWholePage = false)
                            res.result?.let {
                                state = state.copy(allJobsList = it)
                            }
                        }
                    }
                }
            }
        }
    }


    // --- Used to send event to ui
    sealed class JobsUiEvent {
        object Success : JobsUiEvent()
        data class Failure(val errorMessage: String) : JobsUiEvent()
    }
}