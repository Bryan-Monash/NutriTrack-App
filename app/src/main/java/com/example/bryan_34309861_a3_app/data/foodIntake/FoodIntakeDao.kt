package com.example.bryan_34309861_a3_app.data.foodIntake

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodIntakeDao {
    @Insert
    suspend fun insert(foodIntake: FoodIntake)

    @Update
    suspend fun updateFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intake")
    fun getAllFoodIntake(): Flow<List<FoodIntake>>

    @Query("SELECT * FROM food_intake WHERE patientId = :patientId")
    fun getIntakesByPatientId(patientId: String): LiveData<FoodIntake?>
}