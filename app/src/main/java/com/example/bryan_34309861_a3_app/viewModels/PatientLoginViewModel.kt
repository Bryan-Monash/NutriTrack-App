package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.PatientsDashboardScreen
import com.example.bryan_34309861_a3_app.data.AuthManager
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientRepository
import com.example.bryan_34309861_a3_app.utils.UiState
import kotlinx.coroutines.launch

class PatientLoginViewModel(context: Context): ViewModel() {
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
        context: Context,
        navController: NavHostController
    ): () -> Unit {
        if (patientId == "") {
            return { Toast.makeText(context, "No Patient ID selected", Toast.LENGTH_SHORT).show() }
        }
        if (thePatient == null) return {
            Toast.makeText(context,"Patient not in database", Toast.LENGTH_SHORT).show()
        }
        else if (thePatient.value?.patientPassword == "") return {
            Toast.makeText(context, "Patient does not have a password", Toast.LENGTH_SHORT).show()
        }
        else if (thePatient.value?.patientPassword != password) return {
            Toast.makeText(context, "Password is incorrect", Toast.LENGTH_SHORT).show()
        }
        else return {
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            AuthManager.login(patientId)
            navController.navigate(PatientsDashboardScreen.Questionnaire.route)
        }
    }

    class PatientLoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientLoginViewModel(context) as T
    }
}