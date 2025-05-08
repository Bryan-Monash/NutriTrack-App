package com.example.bryan_34309861_a3_app.data.nutriCoachTip

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NutriCoachTipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(nutriCoachTip: NutriCoachTip)

    @Query("SELECT * FROM nutricoachtips WHERE patientId = :patientId " +
            "ORDER BY timeAdded DESC")
    suspend fun getTipsByPatientId(patientId: String): List<NutriCoachTip>
}