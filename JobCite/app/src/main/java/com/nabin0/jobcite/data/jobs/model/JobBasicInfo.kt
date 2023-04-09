package com.nabin0.jobcite.data.jobs.model


import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobBasicInfo(
    @SerializedName("Industry:")
    val industry: String?,
    @SerializedName("Job Function:")
    val jobFunction: String?,
    @SerializedName("Qualification:")
    val qualification: String?,
    @SerializedName("Role:")
    val role: String?,
    @SerializedName("Specialization:")
    val specialization: String?
) : Parcelable