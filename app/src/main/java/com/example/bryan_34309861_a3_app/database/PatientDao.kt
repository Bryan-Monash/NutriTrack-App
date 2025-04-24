package com.example.bryan_34309861_a3_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert
    suspend fun insert(patient: Patient)

    @Update
    suspend fun update(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Query("DELETE FROM patients")
    suspend fun deleteAllPatients()

    @Query("DELETE FROM patients WHERE userId = :patientId")
    suspend fun deletePatientById(patientId: Int)

    @Query("SELECT * FROM patients ORDER BY userId ASC")
    fun getAllPatients(): Flow<List<Patient>>
}