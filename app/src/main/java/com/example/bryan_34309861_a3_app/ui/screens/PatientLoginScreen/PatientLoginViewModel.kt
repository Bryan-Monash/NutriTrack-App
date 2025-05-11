package com.example.bryan_34309861_a3_app.ui.screens.PatientLoginScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.data.util.AuthManager
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
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
        if (thePatient.value == null) return {
            Toast.makeText(context,"Patient not in database", Toast.LENGTH_SHORT).show()
        }

        val hashedPassword = thePatient.value?.patientPassword
        if (hashedPassword.isNullOrEmpty()) return {
            Toast.makeText(context, "Patient does not have a password", Toast.LENGTH_SHORT).show()
        }

        val isPasswordCorrect = BCrypt.verifyer()
            .verify(password.toCharArray(), hashedPassword).verified
        return if (!isPasswordCorrect) {
            {
                Toast.makeText(context, "Password is incorrect", Toast.LENGTH_SHORT).show()
            }
        } else {
            {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                AuthManager.login(patientId, context)
                navController.navigate(AppDashboardScreen.Questionnaire.route)
            }
        }
    }

    class PatientLoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientLoginViewModel(context) as T
    }
}