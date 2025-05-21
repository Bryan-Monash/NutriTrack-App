package com.example.bryan_34309861_a3_app.ui.screens.InsightScreen

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.data.util.UiState
import com.example.bryan_34309861_a3_app.ui.composables.ErrorContent
import com.example.bryan_34309861_a3_app.ui.composables.Loading

@Composable
fun InsightScreen(
    navController: NavHostController,
    context: Context
) {
    val insightViewModel: InsightViewModel = viewModel(
        factory = InsightViewModel.InsightViewModelFactory(context)
    )

    val uiState = insightViewModel.uiState
        .observeAsState()

    when (val state = uiState.value) {
        is UiState.Loading -> {
            Loading()
        }
        is UiState.Error -> {
            ErrorContent(state.errorMessage)
        }
        is UiState.Success -> {
            InsightContent(
                navController,
                insightViewModel,
                context
            )
        }
        else -> Unit
    }
}

@Composable
fun InsightContent(
    navController: NavHostController,
    insightViewModel: InsightViewModel,
    context: Context
) {
    val scoreMap = insightViewModel.getPatientScore()
    val totalScore = insightViewModel.getPatientTotalScore()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Insights: Food Score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        itemsIndexed(scoreMap) { _, (label, score) ->
            val cappedScore = when (label) {
                "Grains & Cereal", "Whole Grain", "Alcohol", "Water", "Saturated Fat", "Unsaturated Fat" -> 5f
                else -> 10f
            }
            val progressRatio = score / cappedScore
            val trackColor = when {
                progressRatio >= 0.8f -> Color(0xFF4CAF50)   // Green
                progressRatio >= 0.5f -> Color(0xFFFFC107)   // Amber
                else -> Color(0xFFF44336)                   // Red
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(8.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProgressBarWithIndicator(
                        progress = progressRatio,
                        progressColor = trackColor,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                    )

                    Text(
                        text = "${score} / ${cappedScore}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }


        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Total Food Quality Score",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                ProgressBarWithIndicator(
                    progress = totalScore / 100,
                    progressColor = Color.Blue,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Text(
                "$totalScore / 100",
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val shareIntent = Intent(ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Hi! I've got a HEIFA score of $totalScore!")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text("Share with someone")
            }
        }

        item {
            Button(
                onClick = {
                    navController.navigate(AppDashboardScreen.NutriCoach.route)
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text("Improve my diet!")
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun ProgressBarWithIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    barColor: Color = Color.LightGray,
    progressColor: Color = Color.Green,
    indicatorFillColor: Color = Color.White,
    indicatorBorderColor: Color = Color(0xFF008080),
    height: Dp = 10.dp,
    circleRadius: Dp = 10.dp,
    borderWidth: Dp = 2.dp
) {
    Canvas(modifier = modifier.height(height)) {
        val width = size.width
        val heightPx = size.height
        val progressX = (progress.coerceIn(0f, 1f)) * width
        val center = Offset(progressX, heightPx / 2)

        // Draw background track
        drawRoundRect(
            color = barColor,
            size = size,
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )

        // Draw progress fill
        drawRoundRect(
            color = progressColor,
            size = Size(progressX, heightPx),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )

        // Draw circle border
        drawCircle(
            color = indicatorBorderColor,
            radius = circleRadius.toPx(),
            center = center
        )

        // Draw inner filled circle
        drawCircle(
            color = indicatorFillColor,
            radius = (circleRadius - borderWidth).toPx().coerceAtLeast(0f),
            center = center
        )
    }
}
