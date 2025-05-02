package com.example.bryan_34309861_a3_app.data.patient

import android.content.Context
import com.example.bryan_34309861_a3_app.data.HospitalDatabase
import kotlinx.coroutines.flow.Flow

class PatientRepository(context: Context) {
    private val patientDao = HospitalDatabase.getDatabase(context).patientDao()

    suspend fun insertPatient(patient: Patient){
        patientDao.insert(patient)
    }

    suspend fun getPatientById(patientId: String): Patient {
        return patientDao.getPatientById(patientId)
    }

    suspend fun updatePassword(patientId: String, newPassword:String) {
        patientDao.updatePassword(patientId, newPassword)
    }

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    fun getAllPatientsId(): Flow<List<String>> = patientDao.getAllPatientsId()
}