package com.example.bryan_34309861_a3_app.data.patient

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PatientViewModel(context: Context): ViewModel() {
    val patientRepo = PatientRepository(context)
    val allPatients: Flow<List<Patient>> = patientRepo.getAllPatients()

    suspend fun insert(patient: Patient) = patientRepo.insertPatient(patient)

    fun getAllPatientsId() = patientRepo.getAllPatientsId()

    suspend fun getPatientById(patientId: String): Patient {
        return patientRepo.getPatientById(patientId)
    }

    suspend fun updatePassword(patientId: String, newPassword: String) {
        patientRepo.updatePassword(patientId, newPassword)
    }

    class PatientViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientViewModel(context) as T
    }
}