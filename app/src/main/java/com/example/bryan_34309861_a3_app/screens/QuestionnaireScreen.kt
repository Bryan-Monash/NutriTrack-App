package com.example.bryan_34309861_a3_app.screens

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.PatientsDashboardScreen
import com.example.bryan_34309861_a3_app.R
import com.example.bryan_34309861_a3_app.data.AuthManager
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntake
import com.example.bryan_34309861_a3_app.data.foodIntake.FoodIntakeViewModel

data class PersonaInfo(val imageRes: Int, val descriptionRes: Int, val name: String)

@Composable
fun QuestionnaireScreen(
    foodIntakeViewModel: FoodIntakeViewModel,
    navController: NavHostController
) {
    val currentPatientId: String = AuthManager.getPatientId()?: ""
    val aFoodIntake: State<FoodIntake?> = foodIntakeViewModel.getFoodIntakeByPatientId(currentPatientId)
        .observeAsState()

    Log.d("FOOD INTAKE", "${aFoodIntake.value}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalArrangement = Arrangement.Top
        ) {
            CheckBoxQuestion(foodIntakeViewModel, aFoodIntake)
        }
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Persona(foodIntakeViewModel, aFoodIntake)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalArrangement = Arrangement.Top
        ) {
            Timings(foodIntakeViewModel, aFoodIntake)
        }
        Button(
            onClick = {
                navController.navigate(PatientsDashboardScreen.Home.route)
            }
        ) {
            Text("Save Preferences")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavHostController
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("Food Intake Questionnaire", maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
        }
    )
}

@Composable
fun CheckBoxQuestion(
    foodIntakeViewModel: FoodIntakeViewModel,
    aFoodIntake: State<FoodIntake?>
) {
    val categories = listOf("Fruits", "Vegetables", "Grains", "Red Meat", "Seafood",
        "Poultry", "Fish", "Eggs", "Nuts/Seeds")

    val checkboxStates = aFoodIntake.value?.checkboxes

    Text(stringResource(R.string.foodIntake), fontWeight = FontWeight.Bold)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        for (col in 0 until 3) {
            Column(
                modifier = Modifier.padding(end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                for (row in 0 until 3) {
                    val index = row * 3 + col
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkboxStates?.get(index) ?: false,
                            onCheckedChange = {
                                val updatedCheckboxes = checkboxStates?.toMutableList().apply {
                                    this?.set(index, it)
                                }
                                val updatedFoodIntake =
                                    updatedCheckboxes?.let{ newCheckboxes ->
                                        aFoodIntake.value?.copy(checkboxes = newCheckboxes)
                                    }
                                updatedFoodIntake?.let {
                                    foodIntakeViewModel.updateFoodIntake(it)
                                }
                            }
                        )
                        Text(
                            text = categories[index],
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Persona(
    foodIntakeViewModel: FoodIntakeViewModel,
    aFoodIntake: State<FoodIntake?>
) {
    val modalStates = remember { mutableStateListOf(*Array(6) { false }) }
    var expanded by remember { mutableStateOf(false) }
    val personaList = listOf(
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )
    val persona = aFoodIntake.value?.persona

    Text(
        text = "Your Persona",
        fontWeight = FontWeight.Bold
    )
    Text(
        text = stringResource(R.string.personaDesc),
        fontSize = 12.sp,
        lineHeight = 18.sp
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        for (col in 0 until 3) {
            Column(
                modifier = Modifier.padding(end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                for (row in 0 until 2) {
                    // index for persona from left to right and up to down
                    val index = row * 3 + col
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = { modalStates[index] = true },
                            modifier = Modifier
                                .padding(2.dp),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = personaList[index],
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                        ShowModal(modalStates, index)
                    }
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "Which persona best fits you?",
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .menuAnchor(),
            value = persona ?: "",
            onValueChange = {},
            textStyle = TextStyle(fontSize = 12.sp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            personaList.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        val updatedPersona =
                            aFoodIntake.value?.copy(persona = option)
                        updatedPersona?.let {
                            foodIntakeViewModel.updateFoodIntake(it)
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ShowModal(
    modalStates: SnapshotStateList<Boolean>,
    index: Int
) {
    if (modalStates[index]) {
        val thePersona = getPersonaInfo(index)
        AlertDialog(
            onDismissRequest = { modalStates[index] = false },
            text = { Text(stringResource(thePersona.descriptionRes), textAlign = TextAlign.Center) },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = thePersona.imageRes),
                        contentDescription = "Persona Image",
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        text = thePersona.name,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        modalStates[index] = false
                    }) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}

fun getPersonaInfo(index: Int): PersonaInfo = when (index) {
    0 -> PersonaInfo(R.drawable.persona_1, R.string.healthDevoteeDesc, "Health Devotee")
    1 -> PersonaInfo(R.drawable.persona_2, R.string.mindfulEaterDesc, "Mindful Eater")
    2 -> PersonaInfo(R.drawable.persona_3, R.string.wellnessStriverDesc, "Wellness Striver")
    3 -> PersonaInfo(R.drawable.persona_4, R.string.balanceSeekerDesc, "Balance Seeker")
    4 -> PersonaInfo(R.drawable.persona_5, R.string.healthProcrastinatorDesc, "Health Procrastinator")
    5 -> PersonaInfo(R.drawable.persona_6, R.string.foodCarefreeDesc, "Food Carefree")
    else -> error("No image found")
}

@Composable
fun Timings(
    foodIntakeViewModel: FoodIntakeViewModel,
    aFoodIntake: State<FoodIntake?>
) {
    Text(
        "Timings",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
    InputRow(stringResource(R.string.eatTime), foodIntakeViewModel, aFoodIntake, "eat")
    Spacer(modifier = Modifier.height(8.dp))
    InputRow(stringResource(R.string.sleepTime), foodIntakeViewModel, aFoodIntake, "sleep")
    Spacer(modifier = Modifier.height(8.dp))
    InputRow(stringResource(R.string.wakeUpTime), foodIntakeViewModel, aFoodIntake, "wakeUp")
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun InputRow(
    question: String,
    foodIntakeViewModel: FoodIntakeViewModel,
    aFoodIntake: State<FoodIntake?>,
    timeType: String
) {
    val initialTime =
        when (timeType) {
            "eat" -> aFoodIntake.value?.eatTime
            "sleep" -> aFoodIntake.value?.sleepTime
            "wakeUp" -> aFoodIntake.value?.wakeUpTime
            else -> error("No Timing found")
        }

    Log.d("TIME", "$initialTime")

    val activityTime = remember { mutableStateOf(initialTime) }

    val mTimePickerDialog =
        TimePickerFun(activityTime, timeType, foodIntakeViewModel, aFoodIntake)
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = question,
                fontSize = 12.sp,
                modifier = Modifier.width(280.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .weight(1f)
                    .clickable { mTimePickerDialog.show() }
            ) {
                OutlinedTextField(
                    value = initialTime ?: "",
                    onValueChange = { },
                    textStyle = TextStyle(fontSize = 13.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.DateRange,
                            "time",
                            Modifier
                                .clickable {
                                    mTimePickerDialog.show()
                                }
                                .size(16.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
    }
}


@Composable
fun TimePickerFun(
    mTime: MutableState<String?>,
    timeType: String,
    foodIntakeViewModel: FoodIntakeViewModel,
    aFoodIntake: State<FoodIntake?>
): TimePickerDialog {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()

    val mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = mCalendar.get(Calendar.MINUTE)

    mCalendar.time = Calendar.getInstance().time

    // time picker value
    return TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->
            mTime.value = String.format("%02d:%02d", mHour, mMinute)
            val updatedFoodIntake = when (timeType) {
                "eat" -> aFoodIntake.value?.copy(eatTime = mTime.value?: "")
                "sleep" -> aFoodIntake.value?.copy(sleepTime = mTime.value?: "")
                "wakeUp" -> aFoodIntake.value?.copy(wakeUpTime = mTime.value?: "")
                else -> null
            }
            updatedFoodIntake?.let {
                foodIntakeViewModel.updateFoodIntake(it)
            }
        }, mHour, mMinute, false
    )
}