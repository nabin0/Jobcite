package com.nabin0.jobcite.presentation.home.study_resources

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.study_resources.usecase.StudyResourceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyResourceViewModel @Inject constructor(
    private val studyResourceUseCases: StudyResourceUseCases
) : ViewModel() {

    var state by mutableStateOf(StudyResourceState())

    private val _uiEventChannel = Channel<StudyResourceUiEvents>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()

    init {
        getStudyResourceData()
    }

    fun onEvent(events: StudyResourceScreenEvents) {
        when (events) {
            StudyResourceScreenEvents.OnSearch -> {
                searchStudyResources()
            }
            StudyResourceScreenEvents.OnSearchBarCloseIconClick -> {
                state = state.copy(searchText = "")
                state = state.copy(isSearchBarVisible = false)
                getStudyResourceData()
            }
            StudyResourceScreenEvents.OnSearchIconClick -> {
                state = state.copy(isSearchBarVisible = true)
            }
            is StudyResourceScreenEvents.OnSearchTextChanged -> {
                state = state.copy(searchText = events.searchText)
            }
            StudyResourceScreenEvents.GetStudyResources -> {
                getStudyResourceData()
            }
        }
    }

    private fun searchStudyResources() {
        state = state.copy(loading = true)
        viewModelScope.launch {
            studyResourceUseCases.searchStudyResourceUseCase(state.searchText) { result ->
                viewModelScope.launch {
                    when (result) {
                        is Resource.Failure -> {
                            state = state.copy(loading = false)
                            _uiEventChannel.send(StudyResourceUiEvents.Failure("Failed to retrieve data. ${result.exception.toString()}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loading = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(dataList = result.result.toList())
                            state = state.copy(loading = false)
                        }
                    }
                }
            }
        }
    }

    private fun getStudyResourceData() {
        viewModelScope.launch {
            state = state.copy(loading = true)
            studyResourceUseCases.getStudyResourceUseCases { result ->
                viewModelScope.launch{
                    when (result) {
                        is Resource.Failure -> {
                            state = state.copy(loading = false)
                            _uiEventChannel.send(StudyResourceUiEvents.Failure("Failed to retrieve data. ${result.exception.toString()}"))
                        }
                        Resource.Loading -> {
                            state = state.copy(loading = true)
                        }
                        is Resource.Success -> {
                            state = state.copy(loading = false)
                            state = state.copy(dataList = result.result)
                        }
                    }
                }
            }
        }

    }

    // --- Used to send event to ui
    sealed class StudyResourceUiEvents {
        class Success(val successMessage: String) : StudyResourceUiEvents()
        data class Failure(val errorMessage: String) : StudyResourceUiEvents()
    }

}


