package com.example.bryan_34309861_a3_app.ui.screens.InsightScreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.data.util.AuthManager
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
import com.example.bryan_34309861_a3_app.data.util.UiState
import kotlinx.coroutines.launch

class InsightViewModel(context: Context): ViewModel() {
    /**
     * Repository instance for handling all data operations.
     * This is the single point of contact for the ViewModel to interact with data sources.
     */
    private val repository = PatientRepository(context)

    /**
     * Patient's ID in the current session
     */
    private val patientId = AuthManager.getPatientId()?: ""

    /**
     * Private mutable live data that stores the patient
     * Using LiveData provides a way to observe changes to the data in real time
     */
    private val _thePatient = MutableLiveData<Patient>()

    /**
     * Private mutable live data that determines the state of UI for getting the patient
     */
    private val _uiState = MutableLiveData<UiState>(UiState.Initial)

    /**
     * Public immutable LiveData that exposes the current UI state for fetching the patient
     */
    val uiState: LiveData<UiState>
        get() = _uiState

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
            _uiState.value = UiState.Loading
            try {
                val patient = repository.getPatientById(patientId)
                _thePatient.value = patient
                _uiState.value = UiState.Success("Patient loaded")
            } catch (e:Exception) {
                _uiState.value = UiState.Error("Error loading patient: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Retrieves the total HEIFA score of the patient
     *
     * @return A Float value representing the total HEIFA score
     */
    fun getPatientTotalScore(): Float {
        return _thePatient.value?.totalScore?: 0f
    }

    /**
     * Retrieves all the scores of the patient
     *
     * @return A list of pairs (String, Float) where the string is the label
     * and the Float is the score
     */
    fun getPatientScore(): List<Pair<String, Float>> {
        return listOf(
            "Vegetables" to (_thePatient.value?.vegetableScore ?: 0f),
            "Fruits" to (_thePatient.value?.fruitsScore?: 0f),
            "Grains & Cereal" to (_thePatient.value?.grainsScore?: 0f),
            "Whole Grains" to (_thePatient.value?.wholeGrainsScore?: 0f),
            "Meat & Alternatives" to (_thePatient.value?.meatAlternativesScore?: 0f),
            "Dairy" to (_thePatient.value?.dairyScore?: 0f),
            "Water" to (_thePatient.value?.waterScore?: 0f),
            "Saturated Fat" to (_thePatient.value?.saturatedFatScore?: 0f),
            "Unsaturated Fat" to (_thePatient.value?.unsaturatedFatScore?: 0f),
            "Sodium" to (_thePatient.value?.sodiumScore?: 0f),
            "Sugar" to (_thePatient.value?.sugarScore?: 0f),
            "Alcohol" to (_thePatient.value?.alcoholScore?: 0f),
            "Discretionary" to (_thePatient.value?.discretionaryScore?: 0f)
        )
    }

    // Factory class for creating instances of InsightViewModel
    class InsightViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            InsightViewModel(context) as T
    }
}