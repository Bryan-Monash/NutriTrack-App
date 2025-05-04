package com.example.bryan_34309861_a3_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.PatientsDashboardScreen
import com.example.bryan_34309861_a3_app.R
import com.example.bryan_34309861_a3_app.data.AuthManager
import com.example.bryan_34309861_a3_app.data.patient.PatientViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    patientViewModel: PatientViewModel
) {
    val currentPatientId = AuthManager.getPatientId() ?: ""
    val thePatient = patientViewModel.getPatientById(currentPatientId)
        .observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 1. Hello section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Hello, ${thePatient.value?.name ?: "User"}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.editQuestionnaire),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(0.8f)
                )
                Button(
                    onClick = {
                        navController.navigate(PatientsDashboardScreen.Questionnaire.route)
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", fontSize = 12.sp)
                }
            }

            Image(
                painter = painterResource(R.drawable.homescreen),
                contentDescription = "plateOfFood",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .padding(vertical = 12.dp)
            )
        }

        // 2. "See all scores" section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My Score",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(0.7f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                        .clickable {
                            navController.navigate(PatientsDashboardScreen.Insight.route)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "See all scores", fontSize = 12.sp)
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Insight"
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Show",
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    text = "Your Food Quality score",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                val totalScore = thePatient.value?.totalScore
                Text(
                    text = "$totalScore / 100",
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider()
        }

        // 3. Food Quality Explanation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        "What is the Food Quality Score?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.food_quality_score_desc),
                        fontSize = 12.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}



@Composable
fun MyBottomAppBar(
    navController: NavHostController
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        PatientsDashboardScreen.Home.route,
        PatientsDashboardScreen.Insight.route,
        PatientsDashboardScreen.NutriCoach.route,
        PatientsDashboardScreen.Settings.route
    )
    NavigationBar {
        items.forEachIndexed {index, item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        PatientsDashboardScreen.Home.route -> Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home"
                        )

                        PatientsDashboardScreen.Insight.route -> Icon(
                            Icons.Filled.Info,
                            contentDescription = "Insights"
                        )

                        PatientsDashboardScreen.NutriCoach.route -> Icon(
                            Icons.Filled.Face,
                            contentDescription = "NutriCoach"
                        )

                        PatientsDashboardScreen.Settings.route -> Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                label = { Text(item) },
                onClick = {
                    navController.navigate(item)
                },
                selected = selectedItem == index
            )
        }
    }
}