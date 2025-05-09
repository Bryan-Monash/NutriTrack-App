package com.example.bryan_34309861_a3_app.ui.screens.ClinicianLoginScreen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.R

@Composable
fun ClinicianLoginScreen(
    navController: NavHostController,
    context: Context
) {
    val clinicianLoginViewModel: ClinicianLoginViewModel = viewModel(
        factory = ClinicianLoginViewModel.ClinicianLoginViewModelFactory(context)
    )

    val key = remember { mutableStateOf("") }

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
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = key.value,
            onValueChange = { key.value = it },
            label = { Text(stringResource(R.string.clinicianKey), fontSize = 14.sp) },
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = clinicianLoginViewModel.validateKey(key.value, context,navController),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text("Clinician Login")
        }
    }
}