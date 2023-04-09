package com.nabin0.jobcite.data.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ChatMessageModel(
    val text: String? = "",
    val userId: String? = "",
    val userName: String? = "",
    val timestamp: Long = 0
)