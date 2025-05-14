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
    /**
     * Repository instance for handling all data operations.
     * This is the single point of contact for the ViewModel to interact with data sources.
     */
    private val repository = FoodIntakeRepository(context)

    /**
     * Patient's ID in the current session
     */
    private val patientId = AuthManager.getPatientId()?: ""

    /**
     * Private mutable live data that stores the foodIntake of the patient
     * Using LiveData provides a way to observe changes to the data in real time
     */
    private val _foodIntake = MutableLiveData<FoodIntake>()

    /**
     * Public immutable LiveData that exposes the patient's foodIntake to the observers
     *
     * This property enables the UI to react to changes in the patient's foodIntake data while
     * preventing direct mutation from outside this class.
     */
    val foodIntake: LiveData<FoodIntake>
        get() = _foodIntake

    /**
     * Private mutable live data that determines the state of UI for getting the patient's
     * foodIntake
     */
    private val _uiState = MutableLiveData<UiState>(UiState.Initial)

    /**
     * Public immutable LiveData that exposes the current UI state for fetching the patient's
     * foodIntake
     */
    val uiState: LiveData<UiState>
        get() = _uiState

    /**
     * Initialize the ViewModel by loading the current patient's foodIntake from the repository
     * This ensures data is available as soon as the UI starts observing.
     */
    init {
        loadFoodIntake()
    }

    /**
     * Loads the current patient's foodIntake by fetching the latest data from the repository
     *
     * This method is responsible for loading the observed LiveData with the most current data.
     */
    private fun loadFoodIntake() {
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

    /**
     * Updates the checkbox in the foodIntake object
     *
     * @param foodIntake The foodIntake to be updated
     * @param checkboxes the checkboxes from the UI
     * @param index the index of the checkbox being changed
     */
    fun updateCheckbox(checkboxes: List<Boolean>, index: Int) {
        viewModelScope.launch {
            _foodIntake.value?.let { repository.updateFoodIntakeCheckbox(it, checkboxes, index) }
            loadFoodIntake()
        }
    }

    /**
     * Updates the persona in the foodIntake object
     *
     * @param foodIntake The foodIntake to be updated
     * @param persona the persona from the UI
     */
    fun updatePersona(persona: String) {
        viewModelScope.launch {
            _foodIntake.value?.let { repository.updateFoodIntakePersona(it, persona) }
            loadFoodIntake()
        }
    }

    /**
     * Updates the times in the foodIntake object
     *
     * @param foodIntake The foodIntake to be updated
     * @param timeType the type of time being changed
     * @param time the value of the time
     */
    fun updateTime(timeType: String, time: String) {
        viewModelScope.launch {
            _foodIntake.value?.let { repository.updateFoodIntakeTime(it, timeType, time) }
            loadFoodIntake()
        }
    }

    /**
     * Validates the user's responses in the food intake questionnaire and provides feedback.
     *
     * This function performs the following checks:
     * 1. At least one food item (checkbox) is selected.
     * 2. A persona is selected.
     * 3. All time fields (sleep, eat, wake up) are filled in.
     * 4. Sleep, eat, and wake-up times are not all the same.
     * 5. Eating time is optimal, defined as:
     *    - At least 2 hours before sleep time, AND
     *    - Not during the user's sleep period (between sleep and wake-up).
     *
     * If all validations pass, a success message is shown and the user is navigated to the home screen.
     * If any validation fails, an appropriate error Toast is displayed.
     *
     * This function is typically used as an `onClick` listener for a submit button.
     *
     * @param context The context used for displaying Toast messages.
     * @param navController The NavHostController used for screen navigation upon successful submission.
     * @return A lambda function that should be invoked to perform the validation and possibly navigation.
     *
     * @sample
     * val onClick = validateQuestionnaire(context, navController)
     * Button(onClick = onClick) { Text("Submit") }
     */
    fun validateQuestionnaire(context: Context, navController: NavHostController): () -> Unit {
        return {
            val checkboxes = _foodIntake.value?.checkboxes
            val persona = _foodIntake.value?.persona
            val sleepTime = _foodIntake.value?.sleepTime
            val eatTime = _foodIntake.value?.eatTime
            val wakeUpTime = _foodIntake.value?.wakeUpTime

            val checkboxesValid = checkboxes?.any { it }?: false

            val personaValid = !persona.isNullOrEmpty()

            val timeValid = !sleepTime.isNullOrEmpty() && !eatTime.isNullOrEmpty() && !wakeUpTime.isNullOrEmpty()

            val timesDifferent = sleepTime != eatTime && sleepTime != wakeUpTime && eatTime != wakeUpTime
            val eatTimeOptimal = if (timeValid) {
                try {
                    val timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                    val eat = java.time.LocalTime.parse(eatTime, timeFormatter)
                    val sleep = java.time.LocalTime.parse(sleepTime, timeFormatter)
                    val wake = java.time.LocalTime.parse(wakeUpTime, timeFormatter)

                    val twoHoursBeforeSleep = sleep.minusHours(2)

                    // Handle time crossing midnight
                    val isDuringSleep = if (sleep.isAfter(wake)) {
                        eat.isAfter(sleep) || eat.isBefore(wake)
                    } else {
                        eat.isAfter(sleep) && eat.isBefore(wake)
                    }

                    eat.isBefore(twoHoursBeforeSleep) && !isDuringSleep
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            } else {
                false
            }

            if (checkboxesValid && personaValid && timeValid && timesDifferent && eatTimeOptimal) {
                Toast.makeText(context, "Questionnaire submitted", Toast.LENGTH_SHORT).show()
                navController.navigate(AppDashboardScreen.Home.route)
            } else {
                when {
                    !checkboxesValid -> Toast.makeText(context, "Choose at least one food", Toast.LENGTH_SHORT).show()
                    !personaValid -> Toast.makeText(context, "Choose one persona", Toast.LENGTH_SHORT).show()
                    !timeValid -> Toast.makeText(context, "Please fill in the time", Toast.LENGTH_SHORT).show()
                    !timesDifferent -> Toast.makeText(context, "Eat, Sleep and Wake Up time cannot be the same", Toast.LENGTH_SHORT).show()
                    !eatTimeOptimal -> Toast.makeText(context, "Eat at least 2 hours before sleep and not during sleep", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Factory class for creating instances of QuestionnaireViewModel
    class QuestionnaireViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuestionnaireViewModel(context) as T
    }
}