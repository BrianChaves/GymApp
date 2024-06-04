package com.example.gymapp.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gymapp.TrainingsViewModel
import com.example.gymapp.data.Exercise
import com.example.gymapp.ui.BackButtonComponent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TrainingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    val viewModel = TrainingsViewModel(context)
    val sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
    val username by remember { mutableStateOf(sharedPreferences.getString("username", "")) }
    var exercises = viewModel.exercisesList

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var dayOfWeek by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_WEEK)) }

    val datePickerDialog by remember {
        mutableStateOf(
            DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                    selectedDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(selectedCalendar.time)
                    dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
                }, year, month, day
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (selectedDate.isEmpty()) "Seleccione la fecha deseada" else "Fecha: $selectedDate",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Abrir calendario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.getExercisesForDay(context, dayOfWeek, username!!)
                      },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Mostrar ejercicios")
        }

        ExerciseDisplayList(exercises = exercises)

        Spacer(modifier = Modifier.height(16.dp))

        BackButtonComponent(navController = navController)
    }
}

@Composable
fun ExerciseDisplayList(exercises : List<Exercise>) {
    if (exercises.isEmpty()) {
        Text(text = "No hay ejercicios para fecha: seleccionada")
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(exercises) { exercise ->
                ExerciseItem(exercise)
            }
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
                text = exercise.exerciseName,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = exercise.type,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
