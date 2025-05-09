package com.example.bryan_34309861_a3_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bryan_34309861_a3_app.data.database.NutriCoachTip

@Dao
interface NutriCoachTipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(nutriCoachTip: NutriCoachTip)

    @Query("SELECT * FROM nutricoachtips WHERE patientId = :patientId " +
            "ORDER BY timeAdded DESC")
    suspend fun getTipsByPatientId(patientId: String): List<NutriCoachTip>
}