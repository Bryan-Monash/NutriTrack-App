package com.example.bryan_34309861_a3_app.data.patient

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.bryan_34309861_a3_app.data.AppDatabase
import kotlinx.coroutines.flow.Flow

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


}