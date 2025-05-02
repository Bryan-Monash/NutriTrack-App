package com.example.bryan_34309861_a3_app

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntakeViewModel
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientViewModel
import com.example.bryan_34309861_a3_app.ui.theme.Bryan_34309861_A3_appTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val _context = LocalContext.current
//            val patientViewModel: PatientViewModel = ViewModelProvider(
//                this, PatientViewModel.PatientViewModelFactory(this@MainActivity)
//            )[PatientViewModel::class.java]
//
//            readCSV(_context, "data.csv", patientViewModel)

            Bryan_34309861_A3_appTheme {
                val _context = LocalContext.current
                _context.startActivity(Intent(_context, PatientsDashboard::class.java))
            }
        }
    }
}

fun readCSV(
    context: Context,
    fileName: String,
    patientViewModel: PatientViewModel,
) {
    val assets = context.assets
    try {
        val inputStream = assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1).forEach { line ->
                val values = line.split(",")
                var newPatient: Patient
                Log.d("PATIENT ID", "${values[1]}")
                if (values[2] == "Male") {
                    newPatient = Patient(
                        patientId = values[1],
                        phoneNumber = values[0],
                        sex = values[2],
                        totalScore = values[3].toFloat(),
                        discretionaryScore = values[5].toFloat(),
                        vegetableScore = values[8].toFloat(),
                        fruitsScore = values[19].toFloat(),
                        grainsScore = values[29].toFloat(),
                        wholeGrainsScore = values[33].toFloat(),
                        meatAlternativesScore = values[36].toFloat(),
                        dairyScore = values[40].toFloat(),
                        sodiumScore = values[43].toFloat(),
                        alcoholScore = values[46].toFloat(),
                        waterScore = values[49].toFloat(),
                        sugarScore = values[54].toFloat(),
                        saturatedFatScore = values[57].toFloat(),
                        unsaturatedFatScore = values[60].toFloat(),
                    )
                } else {
                    newPatient = Patient(
                        patientId = values[1],
                        phoneNumber = values[0],
                        sex = values[2],
                        totalScore = values[4].toFloat(),
                        discretionaryScore = values[6].toFloat(),
                        vegetableScore = values[9].toFloat(),
                        fruitsScore = values[20].toFloat(),
                        grainsScore = values[30].toFloat(),
                        wholeGrainsScore = values[34].toFloat(),
                        meatAlternativesScore = values[37].toFloat(),
                        dairyScore = values[41].toFloat(),
                        sodiumScore = values[44].toFloat(),
                        alcoholScore = values[47].toFloat(),
                        waterScore = values[50].toFloat(),
                        sugarScore = values[55].toFloat(),
                        saturatedFatScore = values[58].toFloat(),
                        unsaturatedFatScore = values[61].toFloat()
                    )
                }
                CoroutineScope(Dispatchers.IO).launch {
                    patientViewModel.insert(patient = newPatient)
                    Log.d("NEW PATIENT", "Added new patient: $newPatient")
                }
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
}