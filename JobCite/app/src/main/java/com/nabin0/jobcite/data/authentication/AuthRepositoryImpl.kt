package com.nabin0.jobcite.data.authentication

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.nabin0.jobcite.Constants.LOGIN_STATUS
import com.nabin0.jobcite.Constants.TAG
import com.nabin0.jobcite.Constants.USER_ID
import com.nabin0.jobcite.Constants.USER_NAME
import com.nabin0.jobcite.data.PreferenceManager
import com.nabin0.jobcite.data.utils.Resource
import com.nabin0.jobcite.data.utils.await
import com.nabin0.jobcite.domain.auth.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth, private val preferenceManager: PreferenceManager
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signUp(
        name: String, email: String, password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signIn(
        email: String, password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result != null) {
                return checkEmailVerification(result)
            } else {
                return Resource.Failure(java.lang.Exception("Unknown error occurred"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    private fun checkEmailVerification(result: AuthResult): Resource<FirebaseUser> {
        return if (result.user?.isEmailVerified == true) {
            preferenceManager.putBoolean(LOGIN_STATUS, true)
            result.user?.let {
                preferenceManager.putString(USER_ID, it.uid)
                preferenceManager.putString(USER_NAME, it.displayName.toString())
            }
            Resource.Success(result.user!!)
        } else {
            Resource.Failure(java.lang.Exception("Verify Your email first."))
        }
    }

    override suspend fun sendEmailVerification(): Resource<Boolean> {
        return try {
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun reloadFirebaseUser(): Resource<Boolean> {
        return try {
            firebaseAuth.currentUser?.reload()?.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun revokeAccess(): Resource<Boolean> {
        return try {
            firebaseAuth.currentUser?.delete()?.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean> = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), firebaseAuth.currentUser == null)

    override suspend fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            Log.d(TAG, "resetPassword: success")
        }.addOnFailureListener {
            Log.d(TAG, "resetPassword: failed $it")
        }
    }
}