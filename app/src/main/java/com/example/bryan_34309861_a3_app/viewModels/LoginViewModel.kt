package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientRepository
import com.example.bryan_34309861_a3_app.utils.UiState
import kotlinx.coroutines.launch

class LoginViewModel(context: Context): ViewModel() {
    val patientRepository = PatientRepository(context)

    private val _allPatients = MutableLiveData<List<Patient>>(emptyList())
    val allPatients: LiveData<List<Patient>>
        get() = _allPatients

    private val _thePatient = MutableLiveData<Patient>()
    val thePatient: LiveData<Patient>
        get() = _thePatient

    init {
        loadPatients()
        Log.d("PATIENTS", "${allPatients.value}")
    }

    fun loadPatients() {
        viewModelScope.launch {
            _allPatients.value = patientRepository.getAllPatients()
        }
    }

    fun getPatientById(patientId: String): LiveData<Patient> {
        viewModelScope.launch {
            _thePatient.value = patientRepository.getPatientById(patientId)
        }
        return thePatient
    }

    fun isAuthorized(
        patientId: String,
        password: String,
        thePatient: Patient
    ): UiState {
        if (patientId == "") return UiState.Error("No Patient ID is chosen")
        if (thePatient == null) return UiState.Error("Patient not in database")
        if (thePatient.patientPassword == "") return UiState.Error("Patient does not have a password")
        if (thePatient.patientPassword != password) return UiState.Error("Password is incorrect")

        return UiState.Success("Login Successful")
    }

    class LoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LoginViewModel(context) as T
    }
}