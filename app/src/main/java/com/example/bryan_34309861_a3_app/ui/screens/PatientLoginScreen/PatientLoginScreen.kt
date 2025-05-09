package com.example.bryan_34309861_a3_app.ui.screens.PatientLoginScreen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientLoginScreen(
    navController: NavHostController,
    context: Context
) {
    val patientLoginViewModel: PatientLoginViewModel = viewModel(
        factory = PatientLoginViewModel.PatientLoginViewModelFactory(context)
    )

    val patientId = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val allPatients = patientLoginViewModel.allPatients
        .observeAsState()

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.navigate(AppDashboardScreen.Welcome.route) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
        Text(
            text = stringResource(R.string.patientLogin),
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            OutlinedTextField(
                value = patientId.value,
                onValueChange = {  },
                label = { Text(stringResource(R.string.patientIdLabel), fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                readOnly = true,
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
            ) {
                allPatients.value?.forEach { patient ->
                    DropdownMenuItem(
                        text = { Text(patient.patientId) },
                        onClick = {
                            patientId.value = patient.patientId
                            expanded = !expanded
                            patientLoginViewModel.getPatientById(patientId.value)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(stringResource(R.string.patientPasswordLabel), fontSize = 14.sp) },
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(stringResource(R.string.loginDisclaimer),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = patientLoginViewModel.isAuthorized(
                patientId.value,
                password.value,
                context,
                navController
            ),
            modifier = Modifier.padding(top = 24.dp).fillMaxWidth(0.85f)
        ) {
            Text("Continue")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                navController.navigate(AppDashboardScreen.Register.route)
            },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text("Register")
        }
    }
}