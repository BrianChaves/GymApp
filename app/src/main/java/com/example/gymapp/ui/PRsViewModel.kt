package com.example.gymapp.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gymapp.AdminOpenHelper
import com.example.gymapp.data.Exercise

class PRsViewModel(context: Context) : ViewModel() {
    private val dbHelper = AdminOpenHelper.getInstance(context)

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> get() = _exercises

    fun loadExercises() {
        val exerciseNames = listOf("Bench Press", "Shoulder Press", "Snatch", "Clean", "Deadlift")
        val loadedExercises = exerciseNames.mapNotNull { dbHelper.getExerciseByName(it) }
        _exercises.value = loadedExercises
    }

    fun updateRecord(exercise: Exercise, newRecord: String) {
        exercise.record = newRecord
        dbHelper.updateExerciseRecord(exercise)
        loadExercises() // Reload to reflect changes
    }
}
