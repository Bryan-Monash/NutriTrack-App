package com.example.bryan_34309861_a3_app.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(value: List<Boolean>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<Boolean> {
        val listType = object : TypeToken<List<Boolean>>() {}.type
        return Gson().fromJson(value, listType)
    }
}