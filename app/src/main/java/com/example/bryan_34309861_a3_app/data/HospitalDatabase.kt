package com.example.bryan_34309861_a3_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntake
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntakeDao
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientDao
import com.example.bryan_34309861_a3_app.utils.Converters

@Database(entities = [Patient::class, FoodIntake::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HospitalDatabase: RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao

    companion object {
        @Volatile
        private var Instance: HospitalDatabase? = null

        fun getDatabase(context: Context): HospitalDatabase {
            return Instance?: synchronized(this) {
                Room.databaseBuilder(context, HospitalDatabase::class.java, "patient_database")
                    .build().also { Instance = it }
            }
        }
    }
}