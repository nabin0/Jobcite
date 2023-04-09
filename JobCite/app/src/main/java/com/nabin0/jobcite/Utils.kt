package com.nabin0.jobcite

import java.util.Calendar
import java.util.Date

class Utils {

    companion object {
        fun fromatDateFromDateObject(date: Date): String {
            val calender = Calendar.getInstance()
            calender.time = date

            val day = calender.get(Calendar.DAY_OF_MONTH)
            val month = calender.get(Calendar.MONTH) + 1
            val year = calender.get(Calendar.YEAR)

            return "$day-$month-$year"
        }
    }
}