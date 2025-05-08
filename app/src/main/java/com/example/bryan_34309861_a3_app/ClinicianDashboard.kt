package com.example.bryan_34309861_a3_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bryan_34309861_a3_app.ui.theme.Bryan_34309861_A3_appTheme

sealed class ClinicianDashboardScreen(val route: String) {
    object Login: ClinicianDashboardScreen("Login")
}

class ClinicianDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Bryan_34309861_A3_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClinicianDashboardContent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun ClinicianDashboardContent(
    innerPaddingValues: PaddingValues
) {

}