package com.example.bryan_34309861_a3_app.data.patient

import androidx.lifecycle.LiveData
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
    suspend fun updatePatient(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Query("SELECT * FROM patients ORDER BY patientId ASC")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT patientId FROM patients")
    fun getAllPatientsId(): Flow<List<String>>

    @Query("SELECT * FROM patients WHERE patientId = :patientId")
    fun getPatientById(patientId: String): LiveData<Patient?>
}