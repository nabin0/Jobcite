package com.nabin0.jobcite.presentation.home.jobs_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin0.jobcite.data.jobs.model.JobsModelItem
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.jobs.JobsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedJobsViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel() {

    var state by mutableStateOf(SavedJobsState())

    private val _uiEventChannel = Channel<SavedJobsTaskOperationEvent>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()


    init {
        getAllSavedJobs()
    }


    fun onEvent(events: SavedJobsScreenEvents) {
        when (events) {
            is SavedJobsScreenEvents.deleteJob -> {
                deleteJob(events.jobItem)
            }
            SavedJobsScreenEvents.getAllSavedJobs -> {
                getAllSavedJobs()
            }
            is SavedJobsScreenEvents.saveJob -> {
                saveJobItem(events.jobItem)
            }
            SavedJobsScreenEvents.refresh -> refresh()
        }
    }

    private fun saveJobItem(jobItem: JobsModelItem) {
        viewModelScope.launch {
            jobsRepository.saveJob(jobItem) { result ->
                viewModelScope.launch {
                    when (result) {
                        is Resource.Failure -> {
                            _uiEventChannel.send(SavedJobsTaskOperationEvent.Failure("Failed to save."))
                        }
                        Resource.Loading -> {}
                        is Resource.Success -> _uiEventChannel.send(
                            SavedJobsTaskOperationEvent.Success(
                                "Saved Successfully."
                            )
                        )
                    }
                }
            }
        }
    }

    private fun refresh(){
        state = state.copy(refreshing = true)
        getAllSavedJobs()
        state = state.copy(refreshing = false)
    }

    private fun getAllSavedJobs() {
        viewModelScope.launch {
            state = state.copy(loading = true)
            jobsRepository.getSavedJobs { result ->
                viewModelScope.launch {
                    when (result) {
                        is Resource.Failure -> {
                            state = state.copy(loading = false)
                            _uiEventChannel.send(SavedJobsTaskOperationEvent.Failure(""))
                        }
                        Resource.Loading -> {
                            state = state.copy(loading = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loading = false)
                            state = state.copy(jobsList = result.result)
                        }
                    }
                }
            }
        }
    }

    private fun deleteJob(jobItem: JobsModelItem) {
        viewModelScope.launch {
            jobsRepository.deleteSavedJob(jobItem) {
                viewModelScope.launch {
                    when (it) {
                        is Resource.Failure -> {
                            _uiEventChannel.send(SavedJobsTaskOperationEvent.Failure("Failed to delete."))
                        }
                        Resource.Loading -> {}
                        is Resource.Success ->{
                            getAllSavedJobs()
                            _uiEventChannel.send(
                                SavedJobsTaskOperationEvent.Success(
                                    "Deleted Successfully."
                                )
                            )
                        }
                    }
                }

            }
        }
    }

}

sealed class SavedJobsScreenEvents {
    data class saveJob(val jobItem: JobsModelItem) : SavedJobsScreenEvents()
    data class deleteJob(val jobItem: JobsModelItem) : SavedJobsScreenEvents()
    object getAllSavedJobs : SavedJobsScreenEvents()
    object refresh : SavedJobsScreenEvents()
}

data class SavedJobsState(
    val jobsList: List<JobsModelItem>? = listOf(),
    val loading: Boolean = false,
    val refreshing: Boolean = false
)

sealed class SavedJobsTaskOperationEvent {
    data class Success(val successMsg: String) : SavedJobsTaskOperationEvent()
    data class Failure(val failureMsg: String) : SavedJobsTaskOperationEvent()
}