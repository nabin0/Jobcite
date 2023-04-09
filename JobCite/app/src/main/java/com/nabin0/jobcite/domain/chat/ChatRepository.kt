package com.nabin0.jobcite.domain.chat

import com.nabin0.jobcite.data.chat.ChatMessageModel
import com.nabin0.jobcite.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getAllChats(result: (Resource<List<ChatMessageModel>>) -> Unit)
    suspend fun sendMessage(msg: ChatMessageModel, result: (Resource<Boolean>) -> Unit)
}