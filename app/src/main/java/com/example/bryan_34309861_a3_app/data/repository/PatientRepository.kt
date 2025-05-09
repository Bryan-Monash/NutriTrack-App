package com.example.bryan_34309861_a3_app.data.repository

import android.content.Context
import com.example.bryan_34309861_a3_app.data.database.AppDatabase
import com.example.bryan_34309861_a3_app.data.database.Patient
import com.example.bryan_34309861_a3_app.data.model.PatientsWithFoodIntake

class PatientRepository(context: Context) {
    private val patientDao = AppDatabase.getDatabase(context).patientDao()

    suspend fun insertPatient(patient: Patient) {
        patientDao.insert(patient)
    }

    suspend fun updatePatientDetails(patient: Patient, name: String, password: String) {
        patient.name = name
        patient.patientPassword = password
        patientDao.updatePatient(patient)
    }

    suspend fun getPatientById(patientId: String): Patient =
        patientDao.getPatientById(patientId)

    suspend fun getAllPatients(): List<Patient> {
        return patientDao.getAllPatients()
    }

    suspend fun getAvgScoreBySex(sex: String): Float? {
        return patientDao.getAvgScoreBySex(sex)
    }

    suspend fun getAllData(): List<PatientsWithFoodIntake> {
        return patientDao.getAllData()
    }
}