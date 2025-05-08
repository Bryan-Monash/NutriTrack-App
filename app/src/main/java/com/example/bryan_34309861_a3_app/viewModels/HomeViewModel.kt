package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.data.AuthManager
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientRepository
import com.example.bryan_34309861_a3_app.utils.UiState
import kotlinx.coroutines.launch

class HomeViewModel(context: Context): ViewModel() {
    private val repository = PatientRepository(context)
    private val patientId = AuthManager.getPatientId()?: ""

    private val _thePatient = MutableLiveData<Patient>()
    val thePatient: LiveData<Patient>
        get() = _thePatient

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState>
        get() = _uiState

    init {
        loadPatient(patientId)
    }

    fun loadPatient(patientId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val patient = repository.getPatientById(patientId)
                _thePatient.value = patient
                _uiState.value = UiState.Success("Patient loaded")
            } catch (e:Exception) {
                _uiState.value = UiState.Error("Error loading patient: ${e.localizedMessage}")
            }
        }
    }

    fun getPatientTotalScore(): Float {
        return thePatient.value?.totalScore?: 0f
    }

    fun getPatientName(): String {
        return thePatient.value?.name?: "User"
    }

    class HomeViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(context) as T
    }
}