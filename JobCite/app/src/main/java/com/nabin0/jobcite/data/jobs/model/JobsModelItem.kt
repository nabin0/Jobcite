package com.nabin0.jobcite.data.jobs.model


import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "jobs")
@Parcelize
data class JobsModelItem(

    @PrimaryKey(autoGenerate = true) val idForRoom: Int,

    @SerializedName("company_name")
    val companyName: String?,

    @SerializedName("experience_required")
    val experienceRequired: String?,

    @SerializedName("hiring_company_link")
    val hiringCompanyLink: String?,

    @SerializedName("id")
    val id: Int,

    @SerializedName("job_basic_info")
    @Embedded
    val jobBasicInfo: JobBasicInfo?,

    @SerializedName("job_description")
    val jobDescription: String?,

    @SerializedName("job_post_link")
    val jobPostLink: String?,

    @SerializedName("job_posted_on")
    val jobPostedOn: String?,

    @SerializedName("job_skills")
    val jobSkills: List<String>?,

    @SerializedName("job_title")
    val jobTitle: String?,

    @SerializedName("location")
    val location: String?,

    @SerializedName("salary")
    val salary: String?
) : Parcelable