package com.example.bryan_34309861_a3_app.data.nutriCoachTip

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NutriCoachTips")
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: String,
    val tip: String,
    val timeAdded: String
)