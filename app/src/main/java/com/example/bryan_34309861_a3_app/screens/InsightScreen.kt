package com.example.bryan_34309861_a3_app.screens

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.PatientsDashboardScreen
import com.example.bryan_34309861_a3_app.utils.ErrorScreen
import com.example.bryan_34309861_a3_app.utils.LoadingScreen
import com.example.bryan_34309861_a3_app.utils.UiState
import com.example.bryan_34309861_a3_app.viewModels.InsightViewModel

@Composable
fun InsightScreen(
    navController: NavHostController,
    context: Context
) {
    val _context = LocalContext.current
    val insightViewModel: InsightViewModel = viewModel(
        factory = InsightViewModel.InsightViewModelFactory(context)
    )

    val uiState = insightViewModel.uiState
        .observeAsState()

    when (val state = uiState.value) {
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Error -> {
            ErrorScreen(state.errorMessage)
        }
        is UiState.Success -> {
            InsightContent(
                navController,
                insightViewModel,
                _context
            )
        }
        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightContent(
    navController: NavHostController,
    insightViewModel: InsightViewModel,
    _context: Context
) {
    val scoreMap = insightViewModel.getPatientScore()

    val totalScore = insightViewModel.getPatientTotalScore()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Insights: Food Score", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            itemsIndexed(scoreMap) { _, (label, score) ->
                val cappedScore = when (label) {
                    "Grains & Cereal", "Whole Grain", "Alcohol", "Water", "Saturated Fat", "Unsaturated Fat" -> 5f
                    else -> 10f
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(0.3f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Text label
                        Text(text = "$label: ", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val progressRatio = score / cappedScore
                        val trackColor = when {
                            progressRatio >= 0.8f -> Color.Green
                            progressRatio >= 0.5f -> Color.Yellow
                            else -> Color.Red
                        }
                        LinearProgressIndicator(
                            progress = { score / cappedScore },
                            modifier = Modifier.padding(10.dp),
                            color = when {
                                score / cappedScore >= 0.8 -> Color.Green
                                score / cappedScore >= 0.5 -> Color.Yellow
                                else -> Color.Red
                            },
                            gapSize = (-15).dp,
                            drawStopIndicator = {}
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.2f)
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "$score / $cappedScore",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Total Food Quality Score",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = { (totalScore ?: 0f) / 100F },
                modifier = Modifier.padding(10.dp),
                gapSize = (-15).dp,
                drawStopIndicator = {}
            )
            Text("$totalScore/100")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            val shareIntent = Intent(ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Hi! I've got a HEIFA score of $totalScore!")
            }
            _context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
        }) {
            Text("Share with someone")
        }
        Button(onClick = {
            navController.navigate(PatientsDashboardScreen.NutriCoach.route)
        }) {
            Text("Improve my diet!")
        }
    }
}