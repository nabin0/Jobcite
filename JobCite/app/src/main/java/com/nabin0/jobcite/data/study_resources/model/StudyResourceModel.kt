package com.nabin0.jobcite.data.study_resources.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class StudyResourceModel(
    val title: String? = null,
    val creator: String? = null,
    val link: String? = null,
    val language: String? = null,
    val contentPlatform: String? = null,
    val free: Boolean? = null,
    val submittedDate: Date? = null,
    val description: String? = null,
    val tags: List<String>? = listOf()
) : Parcelable
