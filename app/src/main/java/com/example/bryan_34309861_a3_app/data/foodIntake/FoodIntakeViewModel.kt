package com.example.bryan_34309861_a3_app.data.foodIntake

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FoodIntakeViewModel(context: Context): ViewModel() {
    private val foodIntakeRepo = FoodIntakeRepository(context)

    fun insertFoodIntake(foodIntake: FoodIntake) {
        viewModelScope.launch {
            foodIntakeRepo.insert(foodIntake)
        }
    }

    class FoodIntakeViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FoodIntakeViewModel(context) as T
    }
}