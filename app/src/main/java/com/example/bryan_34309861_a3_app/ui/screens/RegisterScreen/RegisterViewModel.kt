package com.example.bryan_34309861_a3_app.ui.screens.RegisterScreen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
import kotlinx.coroutines.launch
import at.favre.lib.crypto.bcrypt.BCrypt

class RegisterViewModel(context: Context): ViewModel() {
    /**
     * Repository instance for handling all data operations.
     * This is the single point of contact for the ViewModel to interact with data sources.
     */
    private val repository = PatientRepository(context)

    /**
     * Private mutable live data that stores the list of all patients
     * Using LiveData provides a way to observe changes to the data in real time
     */
    private val _allPatients = MutableLiveData<List<Patient>>(emptyList())

    /**
     * Private mutable live data that stores the patient
     * Using LiveData provides a way to observe changes to the data in real time
     */
    private val _thePatient = MutableLiveData<Patient>()

    /**
     * Initialize the ViewModel by loading the list of all patients from the repository
     * This ensures data is available as soon as the UI starts observing.
     */
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
    fun getAllUnregisteredPatient(): List<Patient> {
        return _allPatients.value?.filter { it.name.isEmpty() }?: emptyList()
    }

    /**
     * Updates the patient's name and password
     *
     * @param name The name of the patient
     * @param password The password that the patient set
     */
    private fun updatePatientDetails(name: String, password: String) {
        viewModelScope.launch {
            repository.updatePatientDetails(_thePatient.value!!, name, password)
            loadPatients()
        }
    }

    /**
     * Validates patient registration input and returns a lambda that performs the validation when invoked.
     *
     * This function is used during patient account setup (first-time login) and performs the following checks:
     * 1. Ensures that a valid patient record (`_thePatient`) is selected.
     * 2. Ensures the patient has not already registered (i.e., has no password).
     * 3. Verifies that the entered phone number matches the one in the patient record.
     * 4. Verifies that the entered password and confirmation password match.
     *
     * If all validations pass:
     * - The password is hashed using BCrypt with a cost factor of 12.
     * - The patient's name and hashed password are updated via `updatePatientDetails`.
     * - Navigation proceeds to the Patient Login screen.
     *
     * If any validation fails, a Toast is shown explaining the error.
     *
     * This method is typically used as an `onClick` listener for a "Register" button.
     *
     * @param patientName The name entered by the patient for registration.
     * @param phoneNumber The phone number used for verification against existing data.
     * @param password The password the user wants to set.
     * @param confirmPassword The confirmation of the password to ensure correctness.
     * @param context The context used to show Toast messages.
     * @param navController The navigation controller used to redirect to the login screen after success.
     * @return A lambda function that executes the validation and updates patient data when invoked.
     */
    fun validatePatient(
        patientName: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        context: Context,
        navController: NavHostController
    ): () -> Unit {
        return {
            when {
                _thePatient.value == null -> {
                    Toast.makeText(context, "Please select a valid Patient ID", Toast.LENGTH_SHORT)
                        .show()
                }

                _thePatient.value?.patientPassword != "" -> {
                    Toast.makeText(context, "Patient is already registered", Toast.LENGTH_SHORT)
                        .show()
                }

                _thePatient.value?.phoneNumber != phoneNumber -> {
                    Toast.makeText(context, "Incorrect Phone Number", Toast.LENGTH_SHORT).show()
                }

                password != confirmPassword -> {
                    Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val hashedPassword = BCrypt.withDefaults()
                        .hashToString(12, password.toCharArray())
                    updatePatientDetails(patientName, hashedPassword)
                    navController.navigate(AppDashboardScreen.PatientLogin.route)
                }
            }
        }
    }

    // Factory class for creating instances of RegisterViewModel
    class RegisterViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RegisterViewModel(context) as T
    }
}