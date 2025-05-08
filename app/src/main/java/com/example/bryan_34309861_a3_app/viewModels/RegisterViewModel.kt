package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.PatientsDashboardScreen
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

    fun updatePatientDetails(name: String, password: String) {
        viewModelScope.launch {
            repository.updatePatientDetails(thePatient.value!!, name, password)
            loadPatients()
        }
    }

    fun validatePatient(
        patientName: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        context: Context,
        navController: NavHostController
    ) {
        return when {
            thePatient.value == null -> {
                Toast.makeText(context, "Please select a valid Patient ID", Toast.LENGTH_SHORT).show()
            }
            thePatient.value?.patientPassword != "" -> {
                Toast.makeText(context, "Patient is already registered", Toast.LENGTH_SHORT).show()
            }
            thePatient.value?.phoneNumber != phoneNumber -> {
                Toast.makeText(context, "Incorrect Phone Number", Toast.LENGTH_SHORT).show()
            }
            password != confirmPassword -> {
                Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
            }
            else -> {
                updatePatientDetails(patientName, password)
                navController.navigate(PatientsDashboardScreen.PatientLogin.route)
            }
        }
    }

    class RegisterViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RegisterViewModel(context) as T
    }
}