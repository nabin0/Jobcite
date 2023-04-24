package com.nabin0.jobcite.data.jobs.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nabin0.jobcite.data.jobs.model.JobsModelItem

@Database(entities = [JobsModelItem::class], version = 3, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class JobDatabase : RoomDatabase() {
    abstract fun jobDao(): JobsDao
}

class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }
}