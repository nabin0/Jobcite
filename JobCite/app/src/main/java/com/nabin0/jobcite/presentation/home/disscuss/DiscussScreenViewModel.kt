package com.nabin0.jobcite.presentation.home.disscuss

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin0.jobcite.Constants
import com.nabin0.jobcite.data.PreferenceManager
import com.nabin0.jobcite.data.chat.ChatMessageModel
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.chat.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscussScreenViewModel @Inject constructor(
    private val chatRepository: ChatRepository, private val preferenceManager: PreferenceManager
) : ViewModel() {

    var state by mutableStateOf(DiscussScreenState())

    init{
        getAllChats()
    }

    fun onEvent(discussScreenEvent: DiscussScreenEvent) {
        when (discussScreenEvent) {
            is DiscussScreenEvent.SendMessage -> {
                sendMessage()
            }
            DiscussScreenEvent.GetAllChats -> {
                getAllChats()
            }
            is DiscussScreenEvent.OnMessageTextChange -> {
                state = state.copy(typedMessage = discussScreenEvent.typedText)
            }
        }
    }

    private fun getAllChats() {
        viewModelScope.launch {
            state = state.copy(loading = true)
            chatRepository.getAllChats { result ->
                when (result) {
                    is Resource.Failure -> {
                        state = state.copy(loading = false)
                        Log.d(Constants.TAG, result.exception.toString())
                    }
                    Resource.Loading -> {
                        state = state.copy(loading = true)
                    }
                    is Resource.Success -> {
                        state = state.copy(loading = false)
                        state = state.copy(chatList = result.result)
                        Log.d(Constants.TAG, "chat received ${result.result.size}")
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        if(state.typedMessage.isNotBlank()){
            viewModelScope.launch {
                chatRepository.sendMessage(
                    ChatMessageModel(
                        text = state.typedMessage,
                        timestamp = System.currentTimeMillis(),
                        userName = preferenceManager.getString(Constants.USER_NAME) ?: "Anonymous",
                        userId = preferenceManager.getString(Constants.USER_ID) ?: ""
                    ),
                ) { result ->
                    state = state.copy(typedMessage = "")
                    when (result) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {}
                        is Resource.Success -> {
                            Log.d(Constants.TAG, "Message Send Successfully.")
                        }
                    }
                }
            }
        }
    }
}

data class DiscussScreenState(
    val chatList: List<ChatMessageModel> = listOf(),
    val loading: Boolean = false,
    val typedMessage: String = "",
)

sealed class DiscussScreenEvent {
    object SendMessage : DiscussScreenEvent()
    object GetAllChats : DiscussScreenEvent()
    data class OnMessageTextChange(val typedText: String) : DiscussScreenEvent()
}