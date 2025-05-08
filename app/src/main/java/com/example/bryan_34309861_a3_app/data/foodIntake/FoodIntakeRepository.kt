package com.example.bryan_34309861_a3_app.data.foodIntake

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bryan_34309861_a3_app.data.AppDatabase
import kotlinx.coroutines.flow.Flow

class FoodIntakeRepository(context: Context) {
    private val foodIntakeDao =
        AppDatabase.getDatabase(context).foodIntakeDao()


    suspend fun insert(foodIntake: FoodIntake) {
        foodIntakeDao.insert(foodIntake)
    }

    suspend fun updateFoodIntakeCheckbox(foodIntake: FoodIntake, checkboxes: List<Boolean>, index: Int) {
        val newCheckbox = checkboxes.toMutableList().apply {
            this.set(index, !checkboxes[index])
        }
        val updatedFoodIntake = foodIntake.copy(checkboxes = newCheckbox)
        foodIntakeDao.updateFoodIntake(updatedFoodIntake)
    }

    suspend fun updateFoodIntakePersona(foodIntake: FoodIntake, persona: String) {
        val updatedFoodIntake = foodIntake.copy(persona = persona)
        foodIntakeDao.updateFoodIntake(updatedFoodIntake)
    }

    suspend fun updateFoodIntakeTime(
        foodIntake: FoodIntake,
        timeType: String,
        time: String
    ) {
        val updatedFoodIntake: FoodIntake? = when (timeType) {
            "eat" -> foodIntake.copy(eatTime = time)
            "sleep" -> foodIntake.copy(sleepTime = time)
            "wakeUp" -> foodIntake.copy(wakeUpTime = time)
            else -> null
        }
        updatedFoodIntake?.let { Log.d("UPDATE", "${it.eatTime} ${it.sleepTime} ${it.wakeUpTime}") }
        if (updatedFoodIntake != null) {
            foodIntakeDao.updateFoodIntake(updatedFoodIntake)
        }
    }

    suspend fun getAllIntakesByPatientId(patientId: String): FoodIntake {
        return foodIntakeDao.getIntakesByPatientId(patientId)
    }
}