package com.example.bryan_34309861_a3_app.database

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface FoodIntakeDao {
    @Insert
    suspend fun insert(foodIntake: FoodIntake)

    @Query("SELECT * FROM FoodIntake WHERE id = :userId")
    fun getIntakesForPatients(userId: Int): Flow<List<FoodIntake>>
}