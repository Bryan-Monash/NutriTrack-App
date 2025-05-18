package com.example.bryan_34309861_a3_app.ui.screens.ResetPasswordScreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.AppDashboardScreen
import com.example.bryan_34309861_a3_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavHostController,
    context: Context
) {
    val viewModel: ResetPasswordViewModel = viewModel(
        factory = ResetPasswordViewModel.ResetPasswordViewModelFactory(context)
    )

    var patientId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newConfirmPassword by remember { mutableStateOf("") }

    val allRegisteredPatients = viewModel.getAllRegisteredPatient()

    var expanded by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var newConfirmPasswordVisible by remember { mutableStateOf(false) }
    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(verticalScroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
        Text(
            text = "Reset Password",
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            OutlinedTextField(
                value = patientId,
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
                allRegisteredPatients.forEach { patient ->
                    DropdownMenuItem(
                        text = { Text(patient.patientId) },
                        onClick = {
                            patientId = patient.patientId
                            viewModel.getPatientById(patientId)
                            expanded = !expanded
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number", fontSize = 14.sp) },
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text(stringResource(R.string.patientPasswordLabel), fontSize = 14.sp) },
            visualTransformation = if (newPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (newPasswordVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff
                val description = if (newPasswordVisible) "Hide password"
                else "Show password"
                IconButton(
                    onClick = { newPasswordVisible = !newPasswordVisible }
                ) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = newConfirmPassword,
            onValueChange = { newConfirmPassword = it },
            label = { Text("Confirm Password", fontSize = 14.sp) },
            visualTransformation = if (newConfirmPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (newConfirmPasswordVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff
                val description = if (newConfirmPasswordVisible) "Hide password"
                else "Show password"
                IconButton(
                    onClick = { newConfirmPasswordVisible = !newConfirmPasswordVisible }
                ) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.loginDisclaimer),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = viewModel.resetPassword(
                phoneNumber,
                newPassword,
                newConfirmPassword,
                context,
                navController
            ),
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(0.85f)
        ) {
            Text("Reset Password")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    navController.navigate(AppDashboardScreen.PatientLogin.route)
                }
            )
        }
    }
}