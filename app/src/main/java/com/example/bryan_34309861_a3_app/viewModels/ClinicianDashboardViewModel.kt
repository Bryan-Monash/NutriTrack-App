package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientRepository
import com.example.bryan_34309861_a3_app.utils.UiState
import kotlinx.coroutines.launch

class ClinicianDashboardViewModel(context: Context): ViewModel() {
    private val repository = PatientRepository(context)

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState>
        get() = _uiState

    private val _allPatients = MutableLiveData<List<Patient>>(emptyList())
    val allPatients: LiveData<List<Patient>>
        get() = _allPatients

    init {
        loadPatients()
    }

    fun loadPatients() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val patients = repository.getAllPatients()
                if (patients.isNotEmpty()) {
                    _allPatients.value = patients
                    _uiState.value = UiState.Success("Successfully fetched")
                } else {
                    _uiState.value = UiState.Error("Database is empty")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage?: "Something went wrong")
            }
        }
    }

    fun getAverageScores(): Pair<Float, Float> {
        val maleAvgScore = mutableFloatStateOf(0f)
        val femaleAvgScore = mutableFloatStateOf(0f)

        viewModelScope.launch {
            val maleAvgScoreFromDb = repository.getAverageScoreBySex("male")
            val femaleAvgScoreFromDb = repository.getAverageScoreBySex("female")

            if (maleAvgScoreFromDb != 0f && femaleAvgScoreFromDb != 0f) {
                maleAvgScore.floatValue = maleAvgScoreFromDb
                femaleAvgScore.floatValue = femaleAvgScoreFromDb
            }
        }
        return Pair(maleAvgScore.floatValue, femaleAvgScore.floatValue)
    }

    class ClinicianDashboardViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClinicianDashboardViewModel(context) as T
    }
}