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
    /**
     * Repository instance for handling all data operations.
     * This is the single point of contact for the ViewModel to interact with data sources.
     */
    private val repository = PatientRepository(context)

    /**
     * Current patient's ID in session
     */
    private val patientId = AuthManager.getPatientId()?: ""

    /**
     * Private mutable live data that stores the current patient in session.
     * Using LiveData provides a way to observe changes to the data over time.
     */
    private val _thePatient = MutableLiveData<Patient>()

    /**
     * Public immutable LiveData that exposes the current patient to the observers
     *
     * This property enables the UI to react to changes in the current patient's data while
     * preventing direct mutation from outside this class.
     */
    val thePatient: LiveData<Patient>
        get() = _thePatient

    /**
     * Initialize the ViewModel by loading the current patient in session from the repository
     * This ensures data is available as soon as the UI starts observing.
     */
    init {
        loadPatient()
    }

    /**
     * Loads the current patient by fetching the latest data from the repository
     *
     * This method is responsible for loading the observed LiveData with the
     * most current data.
     */
    private fun loadPatient() {
        viewModelScope.launch {
            _thePatient.value = repository.getPatientById(patientId)
        }
    }

    /**
     * Returns a lambda that logs the current user out and navigates back to the Patient Login screen.
     *
     * This function:
     * - Clears the current session by calling `AuthManager.logout`.
     * - Navigates to the Patient Login screen via the provided `NavHostController`.
     *
     * Typically used as the onClick handler for a "Logout" button.
     *
     * @param navController The navigation controller used to navigate to the login screen.
     * @param context The context required by `AuthManager` for accessing shared preferences.
     * @return A lambda function that performs logout and navigation when invoked.
     */
    fun logout(navController: NavHostController, context: Context): () -> Unit {
        return {
            AuthManager.logout(context)
            navController.navigate(AppDashboardScreen.PatientLogin.route)
        }
    }

    /**
     * Returns a lambda that navigates the user to the Clinician Login screen.
     *
     * This function is typically used to redirect users who want to log in as a clinician.
     * It performs a single action: navigating to `AppDashboardScreen.ClinicianLogin.route`.
     *
     * @param navController The navigation controller used to perform the screen transition.
     * @return A lambda function that navigates to the Clinician Login screen when invoked.
     */
    fun clinicianLogin(navController: NavHostController): () -> Unit {
        return {
            navController.navigate(AppDashboardScreen.ClinicianLogin.route)
        }
    }

    // Factory class for creating instances of HomeViewModel
    class SettingViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(context) as T
    }
}