package com.example.bryan_34309861_a3_app.database

import android.content.Context
import kotlinx.coroutines.flow.Flow

class FoodIntakeRepository {
    var foodIntakeDao: FoodIntakeDao
    constructor(context: Context) {
        foodIntakeDao = PatientDatabase.getDatabase(context).foodIntakeDao()
    }

    suspend fun insert(foodIntake: FoodIntake) {
        foodIntakeDao.insert(foodIntake)
    }

    fun getAllIntakesForPatient(userId: Int): Flow<List<FoodIntake>> {
        return foodIntakeDao.getIntakesForPatients(userId)
    }
}