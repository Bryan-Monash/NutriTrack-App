package com.example.bryan_34309861_a3_app.ui.screens.SettingsScreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.data.util.AuthManager
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
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

    fun logout(navController: NavHostController, context: Context): () -> Unit {
        return {
            AuthManager.logout(context)
            navController.navigate(AppDashboardScreen.PatientLogin.route)
        }
    }

    fun clinicianLogin(navController: NavHostController): () -> Unit {
        return {
            navController.navigate(AppDashboardScreen.ClinicianLogin.route)
        }
    }

    class SettingViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(context) as T
    }
}