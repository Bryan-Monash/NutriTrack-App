package com.example.bryan_34309861_a3_app

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntake
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntakeViewModel
import com.example.bryan_34309861_a3_app.data.patient.Patient
import com.example.bryan_34309861_a3_app.data.patient.PatientViewModel
import com.example.bryan_34309861_a3_app.screens.ClinicianDashboardScreen
import com.example.bryan_34309861_a3_app.screens.ClinicianLoginScreen
import com.example.bryan_34309861_a3_app.screens.HomeScreen
import com.example.bryan_34309861_a3_app.screens.InsightScreen
import com.example.bryan_34309861_a3_app.screens.MyBottomAppBar
import com.example.bryan_34309861_a3_app.screens.MyTopAppBar
import com.example.bryan_34309861_a3_app.screens.NutriCoachScreen
import com.example.bryan_34309861_a3_app.screens.PatientLoginScreen
import com.example.bryan_34309861_a3_app.screens.QuestionnaireScreen
import com.example.bryan_34309861_a3_app.screens.RegisterScreen
import com.example.bryan_34309861_a3_app.screens.SettingsScreen
import com.example.bryan_34309861_a3_app.screens.WelcomeScreen
import com.example.bryan_34309861_a3_app.ui.theme.Bryan_34309861_A3_appTheme
import java.io.BufferedReader
import java.io.InputStreamReader

sealed class AppDashboardScreen(val route: String) {
    object Welcome : AppDashboardScreen("Welcome")
    object PatientLogin : AppDashboardScreen("PatientLogin")
    object Register : AppDashboardScreen("Register")
    object Questionnaire : AppDashboardScreen("Questionnaire")
    object Home : AppDashboardScreen("Home")
    object Insight : AppDashboardScreen("Insight")
    object NutriCoach: AppDashboardScreen("NutriCoach")
    object Settings: AppDashboardScreen("Settings")
    object ClinicianLogin: AppDashboardScreen("ClinicianLogin")
    object ClinicianDashboard: AppDashboardScreen("ClinicianDashboard")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val patientViewModel: PatientViewModel = ViewModelProvider(
                this, PatientViewModel.PatientViewModelFactory(this@MainActivity)
            )[PatientViewModel::class.java]

            val foodIntakeViewModel: FoodIntakeViewModel = ViewModelProvider(
                this, FoodIntakeViewModel.FoodIntakeViewModelFactory(this@MainActivity)
            )[FoodIntakeViewModel::class.java]
            readCSV(context, "data.csv", patientViewModel, foodIntakeViewModel)

            val navController = rememberNavController()
            val currentRoute by navController.currentBackStackEntryAsState()

            Bryan_34309861_A3_appTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute?.destination?.route in listOf(
                                AppDashboardScreen.Home.route,
                                AppDashboardScreen.Insight.route,
                                AppDashboardScreen.NutriCoach.route,
                                AppDashboardScreen.Settings.route
                            )) {
                            MyBottomAppBar(navController)
                        }
                    },
                    topBar = { if (currentRoute?.destination?.route ==
                        AppDashboardScreen.Questionnaire.route) {
                        MyTopAppBar(navController)
                    }
                    }
                ) { innerPadding ->
                    PatientDashboardContent(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
fun PatientDashboardContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavHostPatient(navController, context)
    }
}

@Composable
fun NavHostPatient(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDashboardScreen.Welcome.route,
        modifier = modifier
    ) {
        composable(AppDashboardScreen.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(AppDashboardScreen.PatientLogin.route) {
            PatientLoginScreen(navController, context)
        }
        composable(AppDashboardScreen.Register.route) {
            RegisterScreen(navController, context)
        }
        composable(AppDashboardScreen.Questionnaire.route) {
            QuestionnaireScreen(navController, context)
        }
        composable(AppDashboardScreen.Home.route) {
            HomeScreen(navController, context)
        }
        composable(AppDashboardScreen.Insight.route) {
            InsightScreen(navController, context)
        }
        composable(AppDashboardScreen.NutriCoach.route) {
            NutriCoachScreen(navController, context)
        }
        composable(AppDashboardScreen.Settings.route) {
            SettingsScreen(navController, context)
        }
        composable(AppDashboardScreen.ClinicianLogin.route) {
            ClinicianLoginScreen(navController, context)
        }
        composable(AppDashboardScreen.ClinicianDashboard.route) {
            ClinicianDashboardScreen(navController, context)
        }
    }
}

fun readCSV(
    context: Context,
    fileName: String,
    patientViewModel: PatientViewModel,
    foodIntakeViewModel: FoodIntakeViewModel,
) {
    val sharedPref = context.getSharedPreferences("csvInData", Context.MODE_PRIVATE)
    Log.d("SHARED PREF", "$sharedPref")
    val isRead = sharedPref.getBoolean("isRead", false)
    if (!isRead) {
        val assets = context.assets
        try {
            val inputStream = assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.useLines { lines ->
                lines.drop(1).forEach { line ->
                    val values = line.split(",")
                    var newPatient: Patient
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
                    val newFoodIntake = FoodIntake(
                        patientId = values[1]
                    )
                    patientViewModel.insertPatient(patient = newPatient)
                    foodIntakeViewModel.insertFoodIntake(foodIntake = newFoodIntake)
                    Log.d("NEW PATIENT", "Added new patient: $newPatient")
                }
            }
            sharedPref.edit().apply {
                putBoolean("isRead", true)
                apply()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}

