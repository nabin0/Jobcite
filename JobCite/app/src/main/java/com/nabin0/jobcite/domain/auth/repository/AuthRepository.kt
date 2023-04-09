package com.nabin0.jobcite.domain.auth.repository

import com.google.firebase.auth.FirebaseUser
import com.nabin0.jobcite.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun signIn(email: String, password: String): Resource<FirebaseUser>
    suspend fun sendEmailVerification(): Resource<Boolean>
    suspend fun reloadFirebaseUser(): Resource<Boolean>
    fun signOut()
    suspend fun revokeAccess(): Resource<Boolean>
    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>
}