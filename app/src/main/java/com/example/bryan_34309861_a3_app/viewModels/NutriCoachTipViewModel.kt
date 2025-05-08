package com.example.bryan_34309861_a3_app.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bryan_34309861_a3_app.BuildConfig
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.data.AuthManager
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntake
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntakeRepository
import com.example.bryan_34309861_a3_app.data.nutriCoachTip.NutriCoachTip
import com.example.bryan_34309861_a3_app.data.nutriCoachTip.NutriCoachTipRepository
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientRepository
import com.example.bryan_34309861_a3_app.utils.UiState
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