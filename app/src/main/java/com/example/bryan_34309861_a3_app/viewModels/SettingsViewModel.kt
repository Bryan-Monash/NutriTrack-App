package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
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
import kotlinx.coroutines.launch

class SettingsViewModel(context: Context): ViewModel() {
    private val repository = PatientRepository(context)
    private val patientId = AuthManager.getPatientId()?: ""

    private val _thePatient = MutableLiveData<Patient>()
    val thePatient: LiveData<Patient>
        get() = _thePatient

    init {
        loadPatient(patientId)
    }

    fun loadPatient(patientId: String) {
        viewModelScope.launch {
            _thePatient.value = repository.getPatientById(patientId)
        }
    }

    fun logout(navController: NavHostController): () -> Unit {
        return {
            AuthManager.logout()
            navController.navigate(PatientsDashboardScreen.PatientLogin.route)
        }
    }

    fun clinicianLogin(navController: NavHostController): () -> Unit {
        return {
            navController.navigate(PatientsDashboardScreen.ClinicianLogin.route)
        }
    }

    class SettingViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(context) as T
    }
}