package com.example.bryan_34309861_a3_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bryan_34309861_a3_app.data.database.FoodIntake

@Dao
interface FoodIntakeDao {
    @Insert
    suspend fun insert(foodIntake: FoodIntake)

    @Update
    suspend fun updateFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intake")
    suspend fun getAllFoodIntake(): List<FoodIntake>

    @Query("SELECT * FROM food_intake WHERE patientId = :patientId")
    suspend fun getIntakesByPatientId(patientId: String): FoodIntake
}