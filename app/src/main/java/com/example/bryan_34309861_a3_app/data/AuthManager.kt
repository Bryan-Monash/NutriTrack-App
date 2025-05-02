package com.example.bryan_34309861_a3_app.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object AuthManager {
    val _userId: MutableState<String?> = mutableStateOf(null)

    fun login(userId: String) {
        _userId.value = userId
    }

    fun logout() {
        _userId.value = null
    }

    fun getPatientId(): String? { // wheres the fun
        return  _userId.value
    }
}