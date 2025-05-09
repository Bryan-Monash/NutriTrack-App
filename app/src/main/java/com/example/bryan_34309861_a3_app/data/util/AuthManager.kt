package com.example.bryan_34309861_a3_app.data.util

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object AuthManager {
    private val _userId: MutableState<String?> = mutableStateOf(null)

    fun initializeUserId(context: Context) {
        val sharedPref = context.getSharedPreferences("AppMemo", Context.MODE_PRIVATE)
        val savedUserId = sharedPref.getString("currentSession", null)
        _userId.value = savedUserId
    }

    fun login(userId: String, context: Context) {
        _userId.value = userId
        context.getSharedPreferences("AppMemo", Context.MODE_PRIVATE)
            .edit()
            .apply {
                putString("currentSession", _userId.value)
                    .apply()
            }
    }

    fun logout(context: Context) {
        _userId.value = null
        context.getSharedPreferences("AppMemo", Context.MODE_PRIVATE)
            .edit()
            .putString("currentSession", null)
            .apply()
    }

    fun getPatientId(): String? { // wheres the fun
        return  _userId.value
    }
}