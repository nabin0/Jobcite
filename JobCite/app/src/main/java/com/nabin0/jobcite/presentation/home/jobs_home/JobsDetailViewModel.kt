package com.nabin0.jobcite.presentation.home.jobs_home

import androidx.compose.runtime.mutableStateOf
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
class JobsDetailViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel() {

    var isBookmarked = mutableStateOf(false)

    private val _uiEventChannel = Channel<JobDetailUiEvents>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()


    fun hasJobItem(jobItem: JobsModelItem){
        viewModelScope.launch {
            val result = jobsRepository.hasJobItem(jobItem)
            isBookmarked.value = result
        }
    }

    fun saveJobItem(jobItem: JobsModelItem) {
        viewModelScope.launch {
            jobsRepository.saveJob(jobItem) { result ->
                viewModelScope.launch {
                    when (result) {
                        is Resource.Failure -> {
                            _uiEventChannel.send(JobDetailUiEvents.Failure("Failed to save."))
                        }
                        Resource.Loading -> {}
                        is Resource.Success -> _uiEventChannel.send(
                            JobDetailUiEvents.Success(
                                "Saved Successfully."
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class JobDetailUiEvents {
    data class Success(val successMsg: String) : JobDetailUiEvents()
    data class Failure(val failureMsg: String) : JobDetailUiEvents()
}