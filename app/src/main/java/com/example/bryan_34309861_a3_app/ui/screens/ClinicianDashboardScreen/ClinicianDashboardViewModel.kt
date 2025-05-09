package com.example.bryan_34309861_a3_app.ui.screens.ClinicianDashboardScreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bryan_34309861_a3_app.R
import com.example.bryan_34309861_a3_app.data.repository.PatientRepository
import com.example.bryan_34309861_a3_app.data.model.PatientsWithFoodIntake
import kotlinx.coroutines.launch

class ClinicianDashboardViewModel(context: Context): ViewModel() {
    private val repository = PatientRepository(context)

    private val _allData = MutableLiveData<List<PatientsWithFoodIntake>>(emptyList())
    val allData: LiveData<List<PatientsWithFoodIntake>>
        get() = _allData

    private val _maleScore = MutableLiveData<Float>()
    val maleScore: LiveData<Float>
        get() = _maleScore

    private val _femaleScore = MutableLiveData<Float>()
    val femaleScore: LiveData<Float>
        get() = _femaleScore

    init {
        loadData()
        getAvgScore()
    }

    fun loadData() {
        viewModelScope.launch {
            _allData.value = repository.getAllData()
        }
    }

    fun getAvgScore() {
        viewModelScope.launch {
            val maleAvgScore = repository.getAvgScoreBySex("male")?: 0f
            val femaleAvgScore = repository.getAvgScoreBySex("female")?: 0f

            if (maleAvgScore != 0f && femaleAvgScore != 0f) {
                _maleScore.value = maleAvgScore
                _femaleScore.value = femaleAvgScore
            }
        }
    }

    fun getDataTable(): String {
        val header = """
        | Patient ID | Total Score | Discretionary Score | Vegetable Score | Fruits Score | Grains Score | Whole Grains Score | Meat Alternatives Score | Dairy Score | Alcohol Score | Water Score | Sugar Score | Saturated Fat Score | Unsaturated Fat Score | Checkboxes | Persona | Sleep Time | Eat Time | Wake Up Time |
        |------------|-------------|---------------------|-----------------|--------------|--------------|--------------------|-------------------------|-------------|---------------|-------------|-------------|---------------------|------------------------|------------|---------|------------|----------|---------------|
    """.trimMargin()

        val rows = _allData.value?.joinToString("\n") { it.toTableRow() } ?: ""

        return header + "\n" + rows
    }

    fun getPersonaDescription(context: Context): List<Pair<String, String>> {
        return listOf(
            "Health Devotee" to context.getString(R.string.healthDevoteeDesc),
            "Mindful Eater" to context.getString(R.string.mindfulEaterDesc),
            "Wellness Striver" to context.getString(R.string.wellnessStriverDesc),
            "Balance Seeker" to context.getString(R.string.balanceSeekerDesc),
            "Health Procrastinator" to context.getString(R.string.healthProcrastinatorDesc),
            "Food Carefree" to context.getString(R.string.foodCarefreeDesc)
        )
    }

    fun getPrompt(context: Context): String {
        val personaInfo = getPersonaDescription(context).toMap()
        return """
            Based on the data provided:
            ${getDataTable()}
            
            Info:
            - The Food Intake checkbox is in this order [Fruits, Vegetables, Grains, Red Meat, Seafood,
            Poultry, Fish, Eggs, Nuts/Seeds] 
            - The persona description is as below:
                - Health Devotee: ${personaInfo["Health Devotee"]}
                - Mindful Eater: ${personaInfo["Mindful Eater"]}
                - Wellness Striver: ${personaInfo["Wellness Striver"]}
                - Balance Seeker: ${personaInfo["Balance Seeker"]}
                - Health Procrastinator: ${personaInfo["Health Procrastinator"]}
                - Food Carefree: ${personaInfo["Food Carefree"]}
            - The times are in 24 hours format.
            
            Identify and describe 3 interesting patterns in the data in 1 to 2 sentence(s) and return the analysis in this format:
            1. <PatternPlaceholder>: <DescPlaceholder>
            
            2. <PatternPlaceholder>: <DescPlaceholder>
            
            3. <PatternPlaceholder>: <DescPlaceholder>
            
        """.trimIndent()
    }

    fun extractInsights(text: String): List<Pair<String, String>> {
        val regex = Regex("""\d+\.\s\*\*(.*?)\*\*[:：]?\s*(.*?)((?=\n\d+\.\s\*\*)|$)""", RegexOption.DOT_MATCHES_ALL)
        return regex.findAll(text).map {
            val title = it.groupValues[1].trim() // cleaned headline (no number, no asterisks)
            val description = it.groupValues[2].trim()
            title to description
        }.toList()
    }

    class ClinicianDashboardViewModelFactory(context: Context) : ViewModelProvider.Factory {
        val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClinicianDashboardViewModel(context) as T
    }
}