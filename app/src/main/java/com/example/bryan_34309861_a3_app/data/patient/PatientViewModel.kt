package com.example.bryan_34309861_a3_app.data.patient

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PatientViewModel(context: Context): ViewModel() {
    val patientRepo = PatientRepository(context)
    private val _allPatients = MutableLiveData<List<Patient>>()
    val allPatients: LiveData<List<Patient>>
        get() = _allPatients

    init {
        loadPatients()
    }

    fun insertPatient(patient: Patient) {
        viewModelScope.launch {
            patientRepo.insertPatient(patient)
        }
    }

    fun loadPatients() {
        viewModelScope.launch {
            _allPatients.value = patientRepo.getAllPatients()
        }
    }

//    fun getPatientById(patientId: String): LiveData<Patient> =
//        patientRepo.getPatientById(patientId)

    class PatientViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientViewModel(context) as T
    }
}