package com.example.bryan_34309861_a3_app.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.viewModels.ClinicianDashboardViewModel

@Composable
fun ClinicianDashboardScreen(
    navController: NavHostController,
    context: Context
) {
    val dashboardViewModel: ClinicianDashboardViewModel = viewModel(
        factory = ClinicianDashboardViewModel.ClinicianDashboardViewModelFactory(context)
    )

    val avgScores = dashboardViewModel.getAverageScores()
    val maleScore = avgScores.first
    val femaleScore = avgScores.second

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Clinician Dashboard",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        HEIFARow("Average HEIFA (Male)", maleScore)
        Spacer(modifier = Modifier.height(8.dp))
        HEIFARow("Average HEIFA (Female)", femaleScore)
    }
}

@Composable
fun HEIFARow(
    label: String,
    value: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(6.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$label :",
                fontWeight = FontWeight.Bold,
            )
            Text(text = value.toString())
        }
    }
}