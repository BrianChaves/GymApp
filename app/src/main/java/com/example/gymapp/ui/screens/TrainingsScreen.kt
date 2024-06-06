package com.example.gymapp.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gymapp.data.Exercise
import com.example.gymapp.ui.BackButtonComponent
import com.example.gymapp.ui.TrainingsViewModel
import com.example.gymapp.ui.theme.GymAppTheme
import kotlinx.coroutines.launch

@Composable
fun TrainingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { TrainingsViewModel(context) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(Modifier.align(Alignment.TopCenter)) {
            Row {
                DatePickerField(viewModel = viewModel) {
                    coroutineScope.launch {
                        viewModel.onUsernameChange()
                        viewModel.onDateChange() }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                ExerciseDisplayField(viewModel = viewModel) {
                    coroutineScope.launch { viewModel.onShowExercisesSelected() }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                BackButton(Modifier.align(Alignment.CenterVertically), navController = navController)
            }
        }
    }
}

@Composable
fun BackButton(modifier: Modifier, navController: NavHostController) {
    Divider()
    Spacer(modifier = Modifier.height(16.dp))
    Column (
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButtonComponent(navController = navController)
    }
}

@Composable
fun DatePickerField(viewModel: TrainingsViewModel, onDateChange: () -> Unit) {
    val selectedDate by viewModel.selectedDate.observeAsState(initial = "")
    val datePickerDialog by viewModel.datePickerDialog.observeAsState()
//    val username by viewModel.username.observeAsState(initial = "")

    Column {
        Text(
            text = if (selectedDate.isEmpty()) "Seleccione la fecha deseada" else "Fecha: $selectedDate",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                datePickerDialog?.show()
                onDateChange()
            },
            Modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "show_calendar"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Abrir calendario")
        }
    }
}

@Composable
fun ExerciseDisplayField(viewModel: TrainingsViewModel, onShowExercisesSelected: () -> Unit) {
    val exercises by viewModel.exercisesList.observeAsState(initial = emptyList())

    Column {
        Button(
            onClick = {
                onShowExercisesSelected()
            },
            Modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = "show_exercises"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Mostrar ejercicios")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        if (exercises.isEmpty()) {
            Text(text = "No hay ejercicios para fecha seleccionada",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(exercises) { exercise ->
                    ExerciseItem(exercise)
                }
            }
            Divider()
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row {
            Text(
                text = "Nombre: ${exercise.exerciseName}",
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.Top),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Tipo: ${exercise.type}",
                textAlign = TextAlign.End,
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.Bottom),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TrainingsScreenPreview() {
    val context = LocalContext.current
    GymAppTheme {
        TrainingsScreen(navController = NavHostController(context))
    }
}