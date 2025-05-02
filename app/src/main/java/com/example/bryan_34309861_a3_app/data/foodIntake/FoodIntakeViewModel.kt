package com.example.bryan_34309861_a3_app.data.foodIntake

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.copy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class FoodIntakeViewModel(context: Context): ViewModel() {
    private val foodIntakeRepo = FoodIntakeRepository(context)

    val allFoodIntake: Flow<List<FoodIntake>> = foodIntakeRepo.allFoodIntake

    suspend fun insertFoodIntake(foodIntake: FoodIntake) =
        foodIntakeRepo.insert(foodIntake)

    fun updateFoodIntake(foodIntake: FoodIntake) {
        viewModelScope.launch {
            foodIntakeRepo.updateFoodIntake(foodIntake)
        }
    }

    fun getFoodIntakeByPatientId(patientId: String): LiveData<FoodIntake?> =
        foodIntakeRepo.getAllIntakesByPatientId(patientId)

    class FoodIntakeViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FoodIntakeViewModel(context) as T
    }
}