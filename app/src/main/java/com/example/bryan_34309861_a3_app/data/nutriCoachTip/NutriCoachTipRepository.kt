package com.example.bryan_34309861_a3_app.data.nutriCoachTip

import android.content.Context
import com.example.bryan_34309861_a3_app.data.AppDatabase

class NutriCoachTipRepository(context: Context) {
    val nutriCoachTipDao = AppDatabase.getDatabase(context).nutriCoachDao()

    suspend fun insertTip(tip: NutriCoachTip) {
        nutriCoachTipDao.insertTip(tip)
    }

    suspend fun getTipsByPatientId(patientId: String): List<NutriCoachTip> {
        return nutriCoachTipDao.getTipsByPatientId(patientId)
    }
}

