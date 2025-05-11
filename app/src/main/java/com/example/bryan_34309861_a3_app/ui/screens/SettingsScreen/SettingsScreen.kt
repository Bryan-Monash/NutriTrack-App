package com.example.bryan_34309861_a3_app.ui.screens.SettingsScreen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(
    navController: NavHostController,
    context: Context
) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.SettingViewModelFactory(context)
    )
    val thePatient = settingsViewModel.thePatient
        .observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "ACCOUNT",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingRow(icon = Icons.Default.Person, thePatient.value?.name?: "")
        Spacer(modifier = Modifier.height(16.dp))
        SettingRow(icon = Icons.Default.Phone, thePatient.value?.phoneNumber?: "")
        Spacer(modifier = Modifier.height(16.dp))
        SettingRow(Icons.Default.Badge, thePatient.value?.patientId?: "")
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "OTHER SETTINGS",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingRow(
            icon = Icons.Default.ExitToApp,
            text = "Logout",
            trailingIcon = Icons.Default.ArrowForward,
            onClick = settingsViewModel.logout(navController, context)
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingRow(
            icon = Icons.Default.SupervisorAccount,
            text = "Clinician Login",
            trailingIcon = Icons.Default.ArrowForward,
            onClick = settingsViewModel.clinicianLogin(navController)
        )
    }
}

@Composable
fun SettingRow(
    icon: ImageVector,
    text: String,
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                fontSize = 16.sp,
            )
        }
        if (trailingIcon != null) {
            Icon(
                trailingIcon,
                contentDescription = "Action",
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}