package com.example.bryan_34309861_a3_app.data.repository

import android.content.Context
import android.util.Log
import com.example.bryan_34309861_a3_app.data.database.AppDatabase
import com.example.bryan_34309861_a3_app.data.database.FoodIntake

class FoodIntakeRepository(context: Context) {

    // Get the FoodIntakeDao instance from the database
    private val foodIntakeDao =
        AppDatabase.getDatabase(context).foodIntakeDao()

    /**
     * Insert a new foodIntake into the database
     *
     * @param foodIntake The [FoodIntake] object to be inserted
     */
    suspend fun insert(foodIntake: FoodIntake) {
        foodIntakeDao.insert(foodIntake)
    }

    /**
     * Updates the checkbox in the foodIntake object
     *
     * @param foodIntake The foodIntake to be updated
     * @param checkboxes the checkboxes from the UI
     * @param index the index of the checkbox being changed
     */
    suspend fun updateFoodIntakeCheckbox(foodIntake: FoodIntake, checkboxes: List<Boolean>, index: Int) {
        val newCheckbox = checkboxes.toMutableList().apply {
            this.set(index, !checkboxes[index])
        }
        foodIntake.checkboxes = newCheckbox
        foodIntakeDao.updateFoodIntake(foodIntake)
    }

    /**
     * Updates the persona in the foodIntake object
     *
     * @param foodIntake The foodIntake to be updated
     * @param persona the persona from the UI
     */
    suspend fun updateFoodIntakePersona(foodIntake: FoodIntake, persona: String) {
        foodIntake.persona = persona
        foodIntakeDao.updateFoodIntake(foodIntake)
    }

    /**
     * Updates the times in the foodIntake object
     *
     * @param foodIntake The foodIntake to be updated
     * @param timeType the type of time being changed
     * @param time the value of the time
     */
    suspend fun updateFoodIntakeTime(
        foodIntake: FoodIntake,
        timeType: String,
        time: String
    ) {
        when (timeType) {
            "eat" -> foodIntake.eatTime = time
            "sleep" -> foodIntake.sleepTime = time
            "wakeUp" -> foodIntake.wakeUpTime = time
            else -> null
        }
        foodIntakeDao.updateFoodIntake(foodIntake)
    }

    /**
     * Retrieve the food intake of a patient based on the
     * patient id
     *
     * @param patientId The patient's ID
     * @return A [FoodIntake] object that corresponds to the
     * patients ID in the database
     */
    suspend fun getAllIntakesByPatientId(patientId: String): FoodIntake {
        return foodIntakeDao.getIntakesByPatientId(patientId)
    }
}