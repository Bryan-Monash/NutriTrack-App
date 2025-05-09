package com.example.bryan_34309861_a3_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_intake")
data class FoodIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: String,
    var checkboxes: List<Boolean> = List(9) { false },
    var persona: String = "",
    var sleepTime: String = "",
    var eatTime: String = "",
    var wakeUpTime: String = ""
)
