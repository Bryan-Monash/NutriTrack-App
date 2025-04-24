package com.example.bryan_34309861_a3_app.database

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PatientViewModel(context: Context): ViewModel() {
    private val patientRepo = PatientRepository(context)
    val allPatients: Flow<List<Patient>> = patientRepo.getAllPatients()

    fun insert(patient: Patient) = viewModelScope.launch {
        patientRepo.insert(patient)
    }

    fun delete(patient: Patient) = viewModelScope.launch {
        patientRepo.delete(patient)
    }

    fun update(patient: Patient) = viewModelScope.launch {
        patientRepo.update(patient)
    }

    fun deleteAllPatients() = viewModelScope.launch {
        patientRepo.deleteAllPatients()
    }

    fun deletePatientById(patientId: Int) = viewModelScope.launch {
        patientRepo.deletePatientById(patientId)
    }

    class PatientViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientViewModel(context) as T
    }
}