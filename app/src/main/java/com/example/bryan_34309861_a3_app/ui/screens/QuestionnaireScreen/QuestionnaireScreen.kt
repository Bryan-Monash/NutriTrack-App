package com.example.bryan_34309861_a3_app.ui.screens.QuestionnaireScreen

import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bryan_34309861_a3_app.R
import com.example.bryan_34309861_a3_app.data.util.UiState
import com.example.bryan_34309861_a3_app.ui.composables.ErrorContent

data class PersonaInfo(val imageRes: Int, val descriptionRes: Int, val name: String)

@Composable
fun QuestionnaireScreen(
    navController: NavHostController,
    context: Context
) {
    val questionnaireViewModel: QuestionnaireViewModel = viewModel(
        factory = QuestionnaireViewModel.QuestionnaireViewModelFactory(context)
    )

    val uiState = questionnaireViewModel.uiState
        .observeAsState()

//    when (val state = uiState.value) {
//        is UiState.Loading -> {
//            QuestionnaireScreenContent(navController, questionnaireViewModel, context)
//        }
//        is UiState.Success -> {
//            QuestionnaireScreenContent(navController, questionnaireViewModel, context)
//        }
//        is UiState.Error -> {
//            ErrorContent(state.errorMessage)
//        }
//        else -> Unit
//    }
    QuestionnaireScreenContent(navController, questionnaireViewModel, context)
}

@Composable
fun QuestionnaireScreenContent(
    navController: NavHostController,
    questionnaireViewModel: QuestionnaireViewModel,
    context: Context
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            CheckBoxQuestion(questionnaireViewModel)
        }

        item {
            HorizontalDivider()
        }

        item {
            Persona(questionnaireViewModel)
        }

        item {
            HorizontalDivider()
        }

        item {
            Timings(questionnaireViewModel)
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = questionnaireViewModel.validateQuestionnaire(context, navController)
                ) {
                    Text("Save Preferences")
                }
            }
        }
    }
}

@Composable
fun CheckBoxQuestion(
    questionnaireViewModel: QuestionnaireViewModel,
) {
    val categories = listOf(
        "Fruits", "Vegetables", "Grains", "Red Meat", "Seafood",
        "Poultry", "Fish", "Eggs", "Nuts/Seeds"
    )

    val checkboxStates = questionnaireViewModel.foodIntake
        .observeAsState().value?.checkboxes ?: List(categories.size) { false }

    Text(
        text = stringResource(R.string.foodIntake),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.chunked(3).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEachIndexed { i, item ->
                    val index = categories.indexOf(item)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = checkboxStates[index],
                            onCheckedChange = {
                                questionnaireViewModel.updateCheckbox(
                                    index = index
                                )
                            }
                        )
                        Text(
                            text = item,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp)
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
    questionnaireViewModel: QuestionnaireViewModel,
) {
    val modalStates = remember { mutableStateListOf(*Array(6) { false }) }
    var expanded by remember { mutableStateOf(false) }
    val personaList = listOf(
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )
    var persona = questionnaireViewModel.foodIntake
        .observeAsState().value?.persona

    Text(
        text = "Your Persona",
        fontWeight = FontWeight.Bold
    )
    Text(
        text = stringResource(R.string.personaDesc),
        fontSize = 12.sp,
        lineHeight = 18.sp
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        personaList.chunked(3).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                rowItems.forEach { name ->
                    val index = personaList.indexOf(name)
                    Button(
                        onClick = { modalStates[index] = true },
                        modifier = Modifier
                            .weight(1f) ,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = name,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            softWrap = true,
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ShowModal(modalStates, index)
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
                        questionnaireViewModel.updatePersona(
                            persona = option
                        )
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
    questionnaireViewModel: QuestionnaireViewModel,
) {
    Text(
        "Timings",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
    InputRow(stringResource(R.string.eatTime), questionnaireViewModel, "eat")
    Spacer(modifier = Modifier.height(10.dp))
    InputRow(stringResource(R.string.sleepTime), questionnaireViewModel,"sleep")
    Spacer(modifier = Modifier.height(10.dp))
    InputRow(stringResource(R.string.wakeUpTime), questionnaireViewModel, "wakeUp")
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun InputRow(
    question: String,
    questionnaireViewModel: QuestionnaireViewModel,
    timeType: String
) {
    val aFoodIntake = questionnaireViewModel.foodIntake
        .observeAsState()
    val initialTime = when (timeType) {
        "eat" -> aFoodIntake.value?.eatTime?: ""
        "sleep" -> aFoodIntake.value?.sleepTime?: ""
        "wakeUp" -> aFoodIntake.value?.wakeUpTime?: ""
        else -> "No Timing found"
    }

    val timeValue = remember(initialTime) { mutableStateOf(initialTime) }

    val mTimePickerDialog =
        TimePickerFun(timeValue, timeType, questionnaireViewModel)
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
                modifier = Modifier.weight(0.7f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .weight(0.3f)
                    .clickable { mTimePickerDialog.show() }
            ) {
//                OutlinedTextField(
//                    value = timeValue.value,
//                    onValueChange = { },
//                    textStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Number
//                    ),
//                    singleLine = true,
//                    enabled = false,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp),
//                    leadingIcon = {
//                        Icon(
//                            Icons.Filled.AccessTime,
//                            "time",
//                            Modifier
//                                .clickable {
//                                    mTimePickerDialog.show()
//                                }
//                                .size(16.dp)
//                        )
//                    },
//                    colors = OutlinedTextFieldDefaults.colors().copy(
//                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
//                        disabledIndicatorColor = MaterialTheme.colorScheme.outline,
//                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
//                )
                ReadOnlyTimeBox(
                    timeText = timeValue.value,
                    onIconClick = {
                        mTimePickerDialog.show()
                    }
                )
            }
        }
    }
}

@Composable
fun TimePickerFun(
    mTime: MutableState<String>,
    timeType: String,
    questionnaireViewModel: QuestionnaireViewModel,
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
            questionnaireViewModel.updateTime(
                timeType = timeType,
                time = mTime.value
            )
        }, mHour, mMinute, false
    )
}

@Composable
fun ReadOnlyTimeBox(
    timeText: String,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onIconClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AccessTime,
                contentDescription = "Pick time",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = timeText,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
