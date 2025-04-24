package com.example.bryan_34309861_a3_app.database

import android.content.Context
import kotlinx.coroutines.flow.Flow

class PatientRepository {
    var patientDao: PatientDao
    constructor(context: Context) {
        patientDao = PatientDatabase.getDatabase(context).patientDao()
    }

    suspend fun insert(patient: Patient){
        patientDao.insert(patient)
    }

    suspend fun delete(patient: Patient){
        patientDao.delete(patient)
    }

    suspend fun update(patient: Patient){
        patientDao.update(patient)
    }

    suspend fun deleteAllPatients() {
        patientDao.deleteAllPatients()
    }

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    suspend fun deletePatientById(id: Int) {
        patientDao.deletePatientById(id)
    }
}