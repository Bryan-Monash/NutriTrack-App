package com.example.bryan_34309861_a3_app.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.model.Fruit
import com.example.bryan_34309861_a3_app.data.repository.FruitApiRepository
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
import com.example.bryan_34309861_a3_app.data.util.AuthManager
import com.example.bryan_34309861_a3_app.data.util.UiState
import kotlinx.coroutines.launch

class FruitApiViewModel(context: Context): ViewModel() {
    private val fruitApiRepository = FruitApiRepository(context)
    private val patientRepository = PatientRepository(context)
    private val patientId = AuthManager.getPatientId()?: ""

    private val _thePatient = MutableLiveData<Patient>()
    val thePatient: LiveData<Patient>
        get() = _thePatient

    private val _patientUiState =  MutableLiveData<UiState>(UiState.Initial)
    val patientUiState: LiveData<UiState>
        get() = _patientUiState

    private val _apiFruit = MutableLiveData<Fruit>()
    val apiFruit: LiveData<Fruit>
        get() = _apiFruit

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState>
        get() = _uiState

    init {
        loadPatient()
    }

    fun loadPatient() {
        viewModelScope.launch {
            _patientUiState.value = UiState.Loading
            val patient = patientRepository.getPatientById(patientId)
            _thePatient.value = patient
            _patientUiState.value = UiState.Success("Fetch successfully")
        }
    }

    fun isFruitScoreOptimal() : Boolean {
        return ((_thePatient.value?.fruitsScore ?: 0f) > 5f)
    }

    fun getFruitDetailByName(fruitName: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val fruit = fruitApiRepository.getFruitDetailsByName(fruitName)
                if (fruit.name != "") {
                    _apiFruit.value = fruit
                    _uiState.value = UiState.Success("Fruit found")
                } else {
                    _uiState.value = UiState.Error("Fruit not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error finding fruit: ${e.localizedMessage}")
            }
        }
    }

    fun getFruitDetailsMap(): List<Pair<String, String>> {
        return listOf(
            "Name" to (apiFruit.value?.name?: ""),
            "Family" to (apiFruit.value?.family?: ""),
            "Genus" to (apiFruit.value?.genus?: ""),
            "Order" to (apiFruit.value?.order?: ""),
            "Calories" to "${apiFruit.value?.nutritions?.calories}",
            "Sugar" to "${apiFruit.value?.nutritions?.sugar}",
            "Carbohydrates" to "${apiFruit.value?.nutritions?.carbohydrates}",
            "Protein" to "${apiFruit.value?.nutritions?.protein}",
            "Fat" to "${apiFruit.value?.nutritions?.fat}"
        )
    }

    class FruitApiViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FruitApiViewModel(context) as T
    }
}