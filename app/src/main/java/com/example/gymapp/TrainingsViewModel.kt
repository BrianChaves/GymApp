package com.example.gymapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.Exercise
import com.example.gymapp.data.Training
import kotlinx.coroutines.launch

class TrainingsViewModel(context: Context) : ViewModel() {

    private val dbHelper = AdminOpenHelper.getInstance(context)

    private var trainingsList = emptyList<Training>()

    private val _exercisesList = mutableListOf<Exercise>()
    val exercisesList : MutableList<Exercise> = _exercisesList

    private fun generateTrainingsByUser(username: String) : List<Training>{
        val exerciseIdRange = 1..70
        fun getRandomExercises(): List<Int> {
            return exerciseIdRange.shuffled().take(20)
        }

        return listOf(
            Training(
                trainingId = username.hashCode(),
                dayOfWeek = 1, // Sunday
                exercises = getRandomExercises()
            ),
            Training(
                trainingId = username.hashCode(),
                dayOfWeek = 2, // Monday
                exercises = getRandomExercises()
            ),
            Training(
                trainingId = username.hashCode(),
                dayOfWeek = 3, // Tuesday
                exercises = getRandomExercises()
            ),
            Training(
                trainingId = username.hashCode(),
                dayOfWeek = 4, // Wednesday
                exercises = getRandomExercises()
            ),
            Training(
                trainingId = username.hashCode(),
                dayOfWeek = 5, // Thursday
                exercises = getRandomExercises()
            ),
            Training(
                trainingId = username.hashCode(),
                dayOfWeek = 6, // Friday
                exercises = getRandomExercises()
            ),
            Training(
                trainingId = username.hashCode(),
                dayOfWeek = 7, // Saturday
                exercises = getRandomExercises()
            )
        )

    }

    private fun getTrainingsList(context: Context, username : String) {
        viewModelScope.launch { trainingsList = dbHelper.loadTrainings(context, username)!! }
    }

    private fun saveTrainings(context: Context, username: String) {
        viewModelScope.launch { dbHelper.saveTrainings(context, username, trainingsList) }
    }

    fun getExercisesForDay(context: Context, dayOfWeek: Int, username: String) {
        viewModelScope.launch {
            getTrainingsList(context, username)
            val list = mutableListOf<Exercise>()
            if (trainingsList.isEmpty()) {
                trainingsList = generateTrainingsByUser(username)
                saveTrainings(context, username)
            } else {
                for (training in trainingsList) {
                    if (training.dayOfWeek == dayOfWeek) training.exercises.forEach {
                        int -> list.add(dbHelper.getExerciseById(int))
                    }
                }
            }
        }
    }
}