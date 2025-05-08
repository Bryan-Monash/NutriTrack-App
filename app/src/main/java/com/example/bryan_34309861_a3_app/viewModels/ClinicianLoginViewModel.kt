package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bryan_34309861_a3_app.utils.UiState

class ClinicianLoginViewModel : ViewModel() {

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState>
        get() = _uiState

    fun isAuthorized(
        adminKey: String
    ) {
        _uiState.value = UiState.Loading

        try {
            if (adminKey == "dollar-entry-apples") _uiState.value =
                UiState.Success("Login successful")
            else _uiState.value = UiState.Error("Wrong key provided")
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.localizedMessage?: "Something went wrong")
        }
    }

    class ClinicianLoginViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClinicianLoginViewModel() as T
    }
}