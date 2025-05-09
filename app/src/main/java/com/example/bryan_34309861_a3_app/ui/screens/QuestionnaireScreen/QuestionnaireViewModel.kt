package com.example.bryan_34309861_a3_app.ui.screens.QuestionnaireScreen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.data.util.AuthManager
import com.example.bryan_34309861_a3_app.data.database.FoodIntake
import com.example.bryan_34309861_a3_app.data.repository.FoodIntakeRepository
import com.example.bryan_34309861_a3_app.data.util.UiState
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

    fun validateQuestionnaire(context: Context, navController: NavHostController): () -> Unit {
        return {
            val checkboxes = _foodIntake.value?.checkboxes
            val persona = _foodIntake.value?.persona
            val sleepTime = _foodIntake.value?.sleepTime
            val eatTime = _foodIntake.value?.eatTime
            val wakeUpTime = _foodIntake.value?.wakeUpTime

            val isCheckboxesValid = checkboxes?.any { it }?: false

            val isPersonaValid = !persona.isNullOrEmpty()

            val isTimeValid = !sleepTime.isNullOrEmpty() && !eatTime.isNullOrEmpty() && !wakeUpTime.isNullOrEmpty()

            val areTimesDifferent = sleepTime != eatTime && sleepTime != wakeUpTime && eatTime != wakeUpTime

            if (isCheckboxesValid && isPersonaValid && isTimeValid && areTimesDifferent) {
                Toast.makeText(context, "Questionnaire submitted", Toast.LENGTH_SHORT).show()
                navController.navigate(AppDashboardScreen.Home.route)
            } else {
                when {
                    !isCheckboxesValid -> Toast.makeText(context, "Choose at least one food", Toast.LENGTH_SHORT).show()
                    !isPersonaValid -> Toast.makeText(context, "Choose one persona", Toast.LENGTH_SHORT).show()
                    !isTimeValid -> Toast.makeText(context, "Please fill in the time", Toast.LENGTH_SHORT).show()
                    !areTimesDifferent -> Toast.makeText(context, "Eat, Sleep and Wake Up time cannot be the same", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    class QuestionnaireViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuestionnaireViewModel(context) as T
    }
}