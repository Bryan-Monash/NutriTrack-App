package com.example.bryan_34309861_a3_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Patient::class], version = 1, exportSchema = false)
abstract class PatientDatabase: RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao

    companion object {
        @Volatile
        private var Instance: PatientDatabase? = null

        fun getDatabase(context: Context): PatientDatabase {
            return Instance?: synchronized(this) {
                Room.databaseBuilder(context, PatientDatabase::class.java, "patient_database")
                    .build().also { Instance = it }
            }
        }
    }
}