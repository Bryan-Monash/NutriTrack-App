package com.example.bryan_34309861_a3_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntakeViewModel
import com.example.bryan_34309861_a3_app.data.patient.PatientViewModel
import com.example.bryan_34309861_a3_app.screens.PatientLoginScreen
import com.example.bryan_34309861_a3_app.screens.QuestionnaireScreen
import com.example.bryan_34309861_a3_app.screens.RegisterScreen
import com.example.bryan_34309861_a3_app.screens.WelcomeScreen
import com.example.bryan_34309861_a3_app.ui.theme.Bryan_34309861_A3_appTheme


sealed class PatientsDashboardScreen(val route: String) {
    object Welcome : PatientsDashboardScreen("welcome")
    object Login : PatientsDashboardScreen("login")
    object Register : PatientsDashboardScreen("register")
    object Questionnaire : PatientsDashboardScreen("questionnaire")
    object Home : PatientsDashboardScreen("home")
    object Insight : PatientsDashboardScreen("insight")
}

class PatientsDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val foodIntakeViewModel: FoodIntakeViewModel = ViewModelProvider(
                this, FoodIntakeViewModel.FoodIntakeViewModelFactory(this@PatientsDashboard)
            )[FoodIntakeViewModel::class.java]

            val patientViewModel: PatientViewModel = ViewModelProvider(
                this, PatientViewModel.PatientViewModelFactory(this@PatientsDashboard)
            )[PatientViewModel::class.java]

            val navController = rememberNavController()
            Bryan_34309861_A3_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PatientDashboardContent(
                        modifier = Modifier.padding(innerPadding),
                        patientViewModel = patientViewModel,
                        foodIntakeViewModel = foodIntakeViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun PatientDashboardContent(
    modifier: Modifier = Modifier,
    patientViewModel: PatientViewModel,
    foodIntakeViewModel: FoodIntakeViewModel,
    navController: NavHostController
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavHostPatient(navController, patientViewModel, foodIntakeViewModel)
    }
}

@Composable
fun NavHostPatient(
    navController: NavHostController,
    patientViewModel: PatientViewModel,
    foodIntakeViewModel: FoodIntakeViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PatientsDashboardScreen.Welcome.route,
        modifier = modifier
    ) {
        composable(PatientsDashboardScreen.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(PatientsDashboardScreen.Login.route) {
            PatientLoginScreen(patientViewModel, navController)
        }
        composable(PatientsDashboardScreen.Register.route) {
            RegisterScreen(navController, patientViewModel)
        }
        composable(PatientsDashboardScreen.Questionnaire.route) {
            QuestionnaireScreen(foodIntakeViewModel, navController)
        }
        composable(PatientsDashboardScreen.Home.route) {

        }
        composable(PatientsDashboardScreen.Insight.route) {

        }
    }
}