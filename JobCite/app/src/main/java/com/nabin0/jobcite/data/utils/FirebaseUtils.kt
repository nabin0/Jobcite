package com.nabin0.jobcite.data.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T{
    return suspendCancellableCoroutine { cortn ->
        addOnCompleteListener { task ->
            if(task.exception != null){
                cortn.resumeWithException(task.exception!!)
            }else{
                cortn.resume(task.result, null)
            }
        }
    }
}