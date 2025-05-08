package com.example.bryan_34309861_a3_app.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.R
import com.example.bryan_34309861_a3_app.utils.LoadingScreen
import com.example.bryan_34309861_a3_app.utils.UiState
import com.example.bryan_34309861_a3_app.viewModels.ClinicianLoginViewModel

@Composable
fun ClinicianLoginScreen(
    navController: NavHostController,
    context: Context
) {
    val clinicianLoginViewModel: ClinicianLoginViewModel = viewModel(
        factory = ClinicianLoginViewModel.ClinicianLoginViewModelFactory()
    )
    val uiState = clinicianLoginViewModel.uiState
        .observeAsState()

    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.clinicianLogin),
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Clinician Key") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                clinicianLoginViewModel.isAuthorized(password.value)

                when (val state = uiState.value) {
                    is UiState.Loading -> { Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show() }
                    is UiState.Success -> { navController.navigate(AppDashboardScreen.ClinicianDashboard.route) }
                    is UiState.Error -> { Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show() }
                    else -> Unit
                }
            },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text(
                text = "Login"
            )
        }
    }
}