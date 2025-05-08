package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientRepository
import kotlinx.coroutines.launch

class RegisterViewModel(context: Context): ViewModel() {
    private val repository = PatientRepository(context)

    val _allPatients = MutableLiveData<List<Patient>>(emptyList())
    val allPatients: LiveData<List<Patient>>
        get() = _allPatients

    val _thePatient = MutableLiveData<Patient>()
    val thePatient: LiveData<Patient>
        get() = _thePatient

    init {
        loadPatients()
    }

    fun loadPatients() {
        viewModelScope.launch {
            _allPatients.value = repository.getAllPatients()
        }
    }

    fun getPatientById(patientId: String): LiveData<Patient> {
        viewModelScope.launch {
            _thePatient.value = repository.getPatientById(patientId)
        }
        return thePatient
    }

    fun updatePatientDetails(patient: Patient, name: String, password: String) {
        viewModelScope.launch {
            repository.updatePatientDetails(patient, name, password)
            loadPatients()
        }
    }

    class RegisterViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RegisterViewModel(context) as T
    }
}