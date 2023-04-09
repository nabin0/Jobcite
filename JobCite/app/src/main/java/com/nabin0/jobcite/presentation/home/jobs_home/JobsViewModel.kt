package com.nabin0.jobcite.presentation.home.jobs_home

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.jobs.JobsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
        state = state.copy(loading = true)
        getJobs()
    }

    fun onEvent(event: JobsScreenEvents) {
        when (event) {
            JobsScreenEvents.getJobs -> {
                getJobs()
            }
            JobsScreenEvents.onSearchBarCloseIconClick -> {
                state = state.copy(searchText = "")
                state = state.copy(isSearchBarVisible = false)
                getJobs()
            }
            JobsScreenEvents.onSearchIconClick -> {
                state = state.copy(isSearchBarVisible = true)
            }
            JobsScreenEvents.searchJobs -> {
                searchJobs()
            }
            is JobsScreenEvents.onSearchTextChange -> {
                state = state.copy(searchText = event.text)
            }
            JobsScreenEvents.onRefresh -> {
                refresh()
            }
        }
    }

    private fun refresh() {
        getJobs()
    }

    private fun searchJobs() {
        viewModelScope.launch {
            state = state.copy(loading = true)
            jobsRepository.searchJobs(state.searchText) { res ->
                viewModelScope.launch {
                    when (res) {
                        is Resource.Failure -> {
                            state = state.copy(loading = false)
                            _uiEventChannel.send(JobsUiEvent.Failure("Error: ${res.exception}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loading = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loading = false)
                            res.result?.let {
                                state = state.copy(jobsList = it)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getJobs() {
        viewModelScope.launch {
            state = state.copy(loading = true)
            jobsRepository.getJobs { res ->
                viewModelScope.launch {
                    when (res) {
                        is Resource.Failure -> {
                            state = state.copy(loading = false)
                            _uiEventChannel.send(JobsUiEvent.Failure("Error: ${res.exception}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loading = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loading = false)
                            res.result?.let {
                                state = state.copy(jobsList = it)
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