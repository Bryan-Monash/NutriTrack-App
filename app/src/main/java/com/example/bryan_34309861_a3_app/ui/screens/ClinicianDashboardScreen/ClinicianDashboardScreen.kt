package com.example.bryan_34309861_a3_app.ui.screens.ClinicianDashboardScreen

import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.data.viewModel.GenAIViewModel
import com.example.bryan_34309861_a3_app.data.util.UiState
import com.example.bryan_34309861_a3_app.ui.composables.ErrorContent
import com.example.bryan_34309861_a3_app.ui.composables.Loading

@Composable
fun ClinicianDashboardScreen(
    navController: NavHostController,
    context: Context
) {
    val dashboardViewModel: ClinicianDashboardViewModel = viewModel(
        factory = ClinicianDashboardViewModel.ClinicianDashboardViewModelFactory(context)
    )

    val maleScore = dashboardViewModel.maleScore
        .observeAsState()
    val femaleScore = dashboardViewModel.femaleScore
        .observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(0.2f)
                .padding(8.dp)
        ) {
            Text(
                text = "Clinician Dashboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            HEIFARow(label = "Average HEIFA (Male)", value = maleScore.value?: 0f)
            Spacer(modifier = Modifier.height(8.dp))
            HEIFARow(label = "Average HEIFA (Female)", value = femaleScore.value?: 0f)
        }
        HorizontalDivider()
        Column(
            modifier = Modifier
                .weight(0.8f)
                .padding(8.dp)
        ) {
            DataAnalysisSection(context, dashboardViewModel)

        }
    }
}

@Composable
fun HEIFARow(label: String, value: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$label :",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(text = "%.2f".format(value))
        }
    }
}

@Composable
fun DataAnalysisSection(
    context: Context,
    dashboardViewModel: ClinicianDashboardViewModel,
    genAIViewModel: GenAIViewModel = viewModel(),
) {
    val prompt = dashboardViewModel.getPrompt(context)
    Log.d("PROMPT", prompt)

    Button(
        onClick = { genAIViewModel.sendPrompt(prompt) },
        modifier = Modifier
            .fillMaxWidth(0.6f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Data Analysis"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Find Data Pattern"
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    DataAnalysisResponse(dashboardViewModel,genAIViewModel)
}

@Composable
fun DataAnalysisResponse(
    dashboardViewModel: ClinicianDashboardViewModel,
    genAIViewModel: GenAIViewModel
) {
    val uiState = genAIViewModel.uiState
        .observeAsState()
    when (val state = uiState.value) {
        is UiState.Loading -> {
            Loading()
        }
        is UiState.Success -> {
            DataAnalysisContent(state.outputText, dashboardViewModel)
        }
        is UiState.Error -> {
            ErrorContent(state.errorMessage)
        }
        else -> Unit
    }
}

@Composable
fun DataAnalysisContent(
    response: String,
    dashboardViewModel: ClinicianDashboardViewModel
) {
    val responses = dashboardViewModel.extractInsights(response)
    Log.d("RESPONSES", "$responses")

    LazyColumn {
        itemsIndexed(responses) { _, (title, description) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(title)
                            }
                            append(" $description")
                        },
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}