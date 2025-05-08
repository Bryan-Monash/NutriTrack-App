package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.data.AuthManager
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntake
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntakeRepository
import com.example.bryan_34309861_a3_app.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionnaireViewModel(context: Context): ViewModel() {
    private val patientId = AuthManager.getPatientId()?: ""
    private val repository = FoodIntakeRepository(context)

    private val _foodIntake = MutableLiveData<FoodIntake>()
    val foodIntake: LiveData<FoodIntake>
        get() = _foodIntake

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState>
        get() = _uiState

    init {
        loadFoodIntake()
    }

    fun loadFoodIntake() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val foodIntake = repository.getAllIntakesByPatientId(patientId)
                _foodIntake.value = foodIntake
                _uiState.value = UiState.Success("Questionnaire fetched successfully")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(" Error loading questionnaire: ${e.localizedMessage}")
            }
        }
    }

    fun updateCheckbox(checkboxes: List<Boolean>, index: Int) {
        viewModelScope.launch {
            foodIntake.value?.let { repository.updateFoodIntakeCheckbox(it, checkboxes, index) }
            loadFoodIntake()
        }
    }

    fun updatePersona(persona: String) {
        viewModelScope.launch {
            foodIntake.value?.let { repository.updateFoodIntakePersona(it, persona) }
            loadFoodIntake()
        }
    }

    fun updateTime(timeType: String, time: String) {
        viewModelScope.launch {
            foodIntake.value?.let { repository.updateFoodIntakeTime(it, timeType, time) }
            loadFoodIntake()
        }
    }

    class QuestionnaireViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuestionnaireViewModel(context) as T
    }
}