package com.example.bryan_34309861_a3_app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String,
    val phoneNumber: Int,
    val sex: String,
    val totalScore: Float,
    val discretionaryScore: Float,
    val vegetableScore: Float,
    val fruitsScore: Float,
    val grainsScore: Float,
    val wholeGrainsScore: Float,
    val meatAlternativesScore: Float,
    val dairyScore: Float,
    val sodiumScore: Float,
    val alcoholScore: Float,
    val waterScore: Float,
    val sugarScore: Float,
    val saturatedFatScore: Float,
    val unsaturatedFatScore: Float
)