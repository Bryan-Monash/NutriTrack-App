package com.example.bryan_34309861_a3_app.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["userId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class FoodIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val checkbox1: Boolean = false,
    val checkbox2: Boolean = false,
    val checkbox3: Boolean = false,
    val checkbox4: Boolean = false,
    val checkbox5: Boolean = false,
    val checkbox6: Boolean = false,
    val checkbox7: Boolean = false,
    val checkbox8: Boolean = false,
    val checkbox9: Boolean = false,
    val persona: String = "",
    val sleepTime: String = "",
    val eatTime: String = "",
    val wakeUpTime: String = ""
)
