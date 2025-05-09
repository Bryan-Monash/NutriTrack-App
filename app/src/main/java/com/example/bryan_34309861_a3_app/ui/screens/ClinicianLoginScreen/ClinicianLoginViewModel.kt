package com.example.bryan_34309861_a3_app.ui.screens.ClinicianLoginScreen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen

class ClinicianLoginViewModel(context: Context): ViewModel() {
    private val key: String = "dollar-entry-apples"

    fun validateKey(
        inputKey: String,
        context: Context,
        navController: NavHostController
    ): () -> Unit {
        return {
            if (inputKey == key) {
                navController.navigate(AppDashboardScreen.ClinicianDashboard.route)
            } else {
                Toast.makeText(context, "Incorrect key", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class ClinicianLoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
        val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClinicianLoginViewModel(context) as T
    }
}