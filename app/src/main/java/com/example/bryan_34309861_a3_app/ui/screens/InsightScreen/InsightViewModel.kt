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
    private val repository = PatientRepository(context)
    private val patientId = AuthManager.getPatientId()?: ""

    private val _thePatient = MutableLiveData<Patient>()
    val thePatient: LiveData<Patient>
        get() = _thePatient

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState>
        get() = _uiState

    init {
        loadPatient(patientId)
    }

    fun loadPatient(patientId: String) {
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

    fun getPatientTotalScore(): Float {
        return thePatient.value?.totalScore?: 0f
    }

    fun getPatientScore(): List<Pair<String, Float>> {
        return listOf(
            "Vegetables" to (thePatient.value?.vegetableScore ?: 0f),
            "Fruits" to (thePatient.value?.fruitsScore?: 0f),
            "Grains & Cereal" to (thePatient.value?.grainsScore?: 0f),
            "Whole Grains" to (thePatient.value?.wholeGrainsScore?: 0f),
            "Meat & Alternatives" to (thePatient.value?.meatAlternativesScore?: 0f),
            "Dairy" to (thePatient.value?.dairyScore?: 0f),
            "Water" to (thePatient.value?.waterScore?: 0f),
            "Saturated Fat" to (thePatient.value?.saturatedFatScore?: 0f),
            "Unsaturated Fat" to (thePatient.value?.unsaturatedFatScore?: 0f),
            "Sodium" to (thePatient.value?.sodiumScore?: 0f),
            "Sugar" to (thePatient.value?.sugarScore?: 0f),
            "Alcohol" to (thePatient.value?.alcoholScore?: 0f),
            "Discretionary" to (thePatient.value?.discretionaryScore?: 0f)
        )
    }

    class InsightViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            InsightViewModel(context) as T
    }
}