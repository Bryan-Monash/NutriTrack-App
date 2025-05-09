package com.example.bryan_34309861_a3_app.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bryan_34309861_a3_app.AppDashboardScreen

@Composable
fun MyBottomAppBar(
    navController: NavHostController
) {
    val items = listOf(
        AppDashboardScreen.Home.route,
        AppDashboardScreen.Insight.route,
        AppDashboardScreen.NutriCoach.route,
        AppDashboardScreen.Settings.route
    )
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item
            NavigationBarItem(
                icon = {
                    when (item) {
                        AppDashboardScreen.Home.route -> Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home"
                        )

                        AppDashboardScreen.Insight.route -> Icon(
                            Icons.Filled.Info,
                            contentDescription = "Insights"
                        )

                        AppDashboardScreen.NutriCoach.route -> Icon(
                            Icons.Filled.Face,
                            contentDescription = "NutriCoach"
                        )

                        AppDashboardScreen.Settings.route -> Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                label = { Text(item) },
                onClick = {
                    navController.navigate(item)
                },
                selected = isSelected
            )
        }
    }
}