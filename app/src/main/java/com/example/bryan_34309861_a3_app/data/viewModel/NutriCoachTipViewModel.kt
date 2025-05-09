package com.example.bryan_34309861_a3_app.data.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.data.util.AuthManager
import com.example.bryan_34309861_a3_app.data.database.FoodIntake
import com.example.bryan_34309861_a3_app.data.repository.FoodIntakeRepository
import com.example.bryan_34309861_a3_app.data.database.NutriCoachTip
import com.example.bryan_34309861_a3_app.data.repository.NutriCoachTipRepository
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
import com.example.bryan_34309861_a3_app.data.util.UiState
import kotlinx.coroutines.launch

class NutriCoachTipViewModel(context: Context): ViewModel() {
    private val nutriRepository = NutriCoachTipRepository(context)
    private val patientRepository = PatientRepository(context)
    private val foodRepository = FoodIntakeRepository(context)
    private val patientId = AuthManager.getPatientId()?: ""

    private val _thePatient = MutableLiveData<Patient>()
    val thePatient: LiveData<Patient>
        get() = _thePatient

    private val _patientUiState = MutableLiveData<UiState>(UiState.Initial)
    val patientUiState: LiveData<UiState>
        get() = _patientUiState

    private val _foodIntake = MutableLiveData<FoodIntake>()
    val foodIntake: LiveData<FoodIntake>
        get() = _foodIntake

    private val _foodUiState = MutableLiveData<UiState>(UiState.Initial)
    val foodUiState: LiveData<UiState>
        get() = _foodUiState

    private val _allTips = MutableLiveData<List<NutriCoachTip>>(emptyList())
    val allTips: LiveData<List<NutriCoachTip>>
        get() = _allTips

    private val _allTipsUiState = MutableLiveData<UiState>(UiState.Initial)
    val allTipsUiState: LiveData<UiState>
        get() = _allTipsUiState

    init {
        loadPatient(patientId)
        loadFoodIntake(patientId)
        loadAllTips()
    }

    fun loadPatient(patientId: String) {
        viewModelScope.launch {
            _patientUiState.value = UiState.Loading
            try {
                val patient = patientRepository.getPatientById(patientId)
                _thePatient.value = patient
                _patientUiState.value = UiState.Success("Patient loaded")
            } catch (e:Exception) {
                _patientUiState.value = UiState.Error("Error loading patient: ${e.localizedMessage}")
            }
        }
    }

    fun loadFoodIntake(patientId: String) {
        viewModelScope.launch {
            _foodUiState.value = UiState.Loading
            try {
                val foodIntake = foodRepository.getAllIntakesByPatientId(patientId)
                _foodIntake.value = foodIntake
                _foodUiState.value = UiState.Success("Questionnaire fetched successfully")
            } catch (e: Exception) {
                _foodUiState.value = UiState.Error(" Error loading questionnaire: ${e.localizedMessage}")
            }
        }
    }

    fun loadAllTips() {
        viewModelScope.launch {
            _allTipsUiState.value = UiState.Loading
            try {
                val tips = nutriRepository.getTipsByPatientId(patientId)
                _allTips.value = tips
                _allTipsUiState.value = UiState.Success("Tips successfully fetched")
            } catch (e: Exception) {
                _allTipsUiState.value = UiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun getPatientScore(): List<Pair<String, Float>> {
        return listOf(
            "Total Score" to (thePatient.value?.totalScore ?: 0f),
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

    fun getPatientFoodIntake(): List<Pair<String, Boolean>> {
        val checkboxes = foodIntake.value?.checkboxes
        return listOf(
            "Fruits" to (checkboxes?.get(0)?: false),
            "Vegetables" to (checkboxes?.get(1)?: false),
            "Grains" to (checkboxes?.get(2)?: false),
            "Red Meat" to (checkboxes?.get(3)?: false),
            "Seafood" to (checkboxes?.get(4)?: false),
            "Poultry" to (checkboxes?.get(5)?: false),
            "Fish" to (checkboxes?.get(6)?: false),
            "Eggs" to (checkboxes?.get(7)?: false),
            "Nuts/Seeds" to (checkboxes?.get(8)?: false),
        )
    }

    fun generatePrompt(): String {
        val scores = getPatientScore().toMap()
        val foodIntake = getPatientFoodIntake().toMap()

        val ateFruits = foodIntake["Fruits"] ?: false

        return """
                Generate a short, friendly, and encouraging message (1–2 sentences) to help someone improve their diet. Here are their HEIFA category scores:
                - Total Score: ${scores["Total Score"]}
                - Vegetables: ${scores["Vegetables"]}
                - Fruits: ${scores["Fruits"]}
                - Grains & Cereal: ${scores["Grains & Cereal"]}
                - Whole Grains: ${scores["Whole Grains"]}
                - Meat & Alternatives: ${scores["Meat & Alternatives"]}
                - Dairy: ${scores["Dairy"]}
                - Water: ${scores["Water"]}
                - Saturated Fat: ${scores["Saturated Fat"]}
                - Unsaturated Fat: ${scores["Unsaturated Fat"]}
                - Sodium: ${scores["Sodium"]}
                - Sugar: ${scores["Sugar"]}
                - Alcohol: ${scores["Alcohol"]}
                - Discretionary: ${scores["Discretionary"]}
                
                They ${if (ateFruits) "reported eating fruits recently" else "did not report eating fruits recently"}. Encourage them to keep up the good habits or improve in a positive, supportive, and motivational tone.
                """.trimIndent()
    }

    fun insertTip(nutriCoachTip: NutriCoachTip) {
        viewModelScope.launch {
            nutriRepository.insertTip(nutriCoachTip)
            loadAllTips()
        }
    }

    class NutriCoachTipViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NutriCoachTipViewModel(context) as T
    }
}