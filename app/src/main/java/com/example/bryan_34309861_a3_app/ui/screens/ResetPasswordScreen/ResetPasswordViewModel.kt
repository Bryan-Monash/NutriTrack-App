package com.example.bryan_34309861_a3_app.ui.screens.ResetPasswordScreen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
import kotlinx.coroutines.launch

class ResetPasswordViewModel(context: Context) : ViewModel() {
    private val repository = PatientRepository(context)

    private val _allPatients = MutableLiveData<List<Patient>>(emptyList())

    private val _thePatient = MutableLiveData<Patient>()

    init {
        loadPatients()
    }

    /**
     * Loads all patients by fetching the latest data from the repository
     *
     * This method is responsible for loading the observed LiveData with the
     * most current data.
     */
    private fun loadPatients() {
        viewModelScope.launch {
            _allPatients.value = repository.getAllPatients()
        }
    }

    /**
     * Retrieve a patient from the database based on their ID.
     *
     * @param patientId The ID of the patient to retrieve.
     * @return The [Patient] object in the database
     */
    fun getPatientById(patientId: String): LiveData<Patient> {
        viewModelScope.launch {
            _thePatient.value = repository.getPatientById(patientId)
        }
        return _thePatient
    }

    /**
     * Retrieve a list of patients that are registered
     *
     * @return A list of patients that are registered
     */
    fun getAllRegisteredPatient(): List<Patient> {
        return _allPatients.value?.filter { it.name.isNotEmpty() }?: emptyList()
    }

    private fun updatePatientPassword(password: String) {
        viewModelScope.launch {
            repository.updatePatientDetails(_thePatient.value!!, _thePatient.value!!.name, password)
            loadPatients()
        }
    }

    fun resetPassword(
        phoneNumber: String,
        newPassword: String,
        newConfirmPassword: String,
        context: Context,
        navController: NavHostController
    ): () -> Unit {
        return {
            when {
                _thePatient.value?.phoneNumber != phoneNumber -> {
                    Toast.makeText(context, "Incorrect Phone Number", Toast.LENGTH_SHORT).show()
                }

                newPassword != newConfirmPassword -> {
                    Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val hashedPassword = BCrypt.withDefaults()
                        .hashToString(12, newPassword.toCharArray())
                    updatePatientPassword(hashedPassword)
                    Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(AppDashboardScreen.PatientLogin.route)
                }
            }
        }
    }

    class ResetPasswordViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ResetPasswordViewModel(context) as T
    }
}