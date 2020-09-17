package com.vau.myappmanager.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("YYYY - MM - dd")
        return format.format(date)
    }
}