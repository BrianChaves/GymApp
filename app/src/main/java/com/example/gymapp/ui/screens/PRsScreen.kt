package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymapp.data.Exercise
import com.example.gymapp.ui.PRsViewModel
import com.example.gymapp.ui.PRsViewModelFactory

@Composable
fun PRsScreen(navController: NavController, viewModel: PRsViewModel = viewModel(factory = PRsViewModelFactory(LocalContext.current))) {
    val exercises by viewModel.exercises.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Personal Records (PRs)", style = MaterialTheme.typography.headlineMedium)

            exercises.forEach { exercise ->
                ExerciseRecordItem(exercise) { newRecord ->
                    viewModel.updateRecord(exercise, newRecord)
                }
                Spacer(modifier = Modifier.size(16.dp))
            }

            Button(onClick = { navController.popBackStack() }) {
                Text(text = "Save PRs")
            }

        }
    }
}

@Composable
fun ExerciseRecordItem(exercise: Exercise, onRecordChange: (String) -> Unit) {
    var record by remember { mutableStateOf(exercise.record ?: "") }

    Column {
        Text(text = exercise.exerciseName, style = MaterialTheme.typography.bodyLarge)
        BasicTextField(
            value = record,
            onValueChange = { newRecord ->
                record = newRecord
                onRecordChange(newRecord)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (record.isEmpty()) {
                    Text(text = "Enter PR for ${exercise.exerciseName}")
                }
                innerTextField()
            }
        )
    }
}
