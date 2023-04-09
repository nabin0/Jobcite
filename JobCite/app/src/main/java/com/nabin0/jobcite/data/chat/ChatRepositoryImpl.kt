package com.nabin0.jobcite.data.chat

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nabin0.jobcite.Constants.COLLECTION_FIREBASE_GLOBAL_CHAT
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.domain.chat.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.Exception

class ChatRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
) : ChatRepository {

    private val globalChatCollection = firebaseFirestore.collection(COLLECTION_FIREBASE_GLOBAL_CHAT)

    override suspend fun getAllChats(result: (Resource<List<ChatMessageModel>>) -> Unit) {
        try {
            globalChatCollection.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        result.invoke(Resource.Failure(error))
                    } else {
                        val chats = value?.toObjects(ChatMessageModel::class.java)
                        result.invoke(Resource.Success(chats ?: emptyList()))
                    }
                }
        } catch (e: Exception) {
            result.invoke(Resource.Failure(e))
        }
    }

    override suspend fun sendMessage(msg: ChatMessageModel, result: (Resource<Boolean>) -> Unit) {
        try {
            globalChatCollection.add(msg).addOnSuccessListener {
                result.invoke(Resource.Success(true))
            }.addOnFailureListener {
                result.invoke(Resource.Failure(it))
            }
        } catch (e: Exception) {
            result.invoke(Resource.Failure(Exception("Unknown error occurred!")))
        }
    }
}