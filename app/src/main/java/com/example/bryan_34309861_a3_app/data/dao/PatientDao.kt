package com.example.bryan_34309861_a3_app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.model.PatientsWithFoodIntake

@Dao
interface PatientDao {
    @Insert
    suspend fun insert(patient: Patient)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Query("SELECT * FROM patients ORDER BY CAST(patientId AS INTEGER) ASC")
    suspend fun getAllPatients(): List<Patient>

    @Query("SELECT * FROM patients WHERE patientId = :patientId")
    suspend fun getPatientById(patientId: String): Patient

    @Query("SELECT AVG(totalScore) AS avgScore FROM patients WHERE upper(sex) = upper(:sex)")
    suspend fun getAvgScoreBySex(sex: String): Float?

    @Query("""
        SELECT
            patients.patientId, totalScore, discretionaryScore, vegetableScore, fruitsScore, 
            grainsScore, wholeGrainsScore, meatAlternativesScore, dairyScore, sodiumScore, 
            alcoholScore, waterScore, sugarScore, saturatedFatScore, unsaturatedFatScore, 
            checkboxes, persona, sleepTime, eatTime, wakeUpTime 
            FROM patients INNER JOIN food_intake ON patients.patientId = food_intake.patientId
    """)
    suspend fun getAllData(): List<PatientsWithFoodIntake>
}