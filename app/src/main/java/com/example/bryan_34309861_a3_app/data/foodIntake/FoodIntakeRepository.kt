package com.example.bryan_34309861_a3_app.data.foodIntake

import android.content.Context
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import com.example.bryan_34309861_a3_app.data.HospitalDatabase
import kotlinx.coroutines.flow.Flow

class FoodIntakeRepository(context: Context) {
    private val foodIntakeDao =
        HospitalDatabase.getDatabase(context).foodIntakeDao()

    val allFoodIntake: Flow<List<FoodIntake>> = foodIntakeDao.getAllFoodIntake()

    suspend fun insert(foodIntake: FoodIntake) {
        foodIntakeDao.insert(foodIntake)
    }

    suspend fun updateFoodIntake(foodIntake: FoodIntake) {
        foodIntakeDao.updateFoodIntake(foodIntake)
    }

    fun getAllIntakesByPatientId(patientId: String): LiveData<FoodIntake?> {
        return foodIntakeDao.getIntakesByPatientId(patientId)
    }
}