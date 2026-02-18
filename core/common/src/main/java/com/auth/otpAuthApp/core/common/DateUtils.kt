package com.auth.otpAuthApp.core.common

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    fun formatDate(isoDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(isoDate)
            
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            date?.let { outputFormat.format(it) } ?: isoDate
        } catch (e: Exception) {
            isoDate // Fallback to original string if parsing fails
        }
    }
}
