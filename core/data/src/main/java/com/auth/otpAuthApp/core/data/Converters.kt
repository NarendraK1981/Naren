package com.auth.otpAuthApp.core.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromReviewList(value: List<Review>): String {
        val type = object : TypeToken<List<Review>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toReviewList(value: String): List<Review> {
        val type = object : TypeToken<List<Review>>() {}.type
        return gson.fromJson(value, type)
    }
}
