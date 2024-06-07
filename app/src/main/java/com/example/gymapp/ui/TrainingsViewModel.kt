package com.example.gymapp.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.widget.DatePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gymapp.AdminOpenHelper
import com.example.gymapp.data.Exercise
import com.example.gymapp.data.Training
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TrainingsViewModel(context: Context) : ViewModel() {

    private val appContext = context
    private val dbHelper = AdminOpenHelper.getInstance(appContext)
    private val sharedPreferences: SharedPreferences = appContext.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    private var trainingsList = emptyList<Training>()

    private val _selectedDate = MutableLiveData("")
    val selectedDate : LiveData<String> = _selectedDate

    private val _dayOfWeek = MutableLiveData(0)
    val dayOfWeek : LiveData<Int> = _dayOfWeek

    private val _datePickerDialog = MutableLiveData<DatePickerDialog>()
    val datePickerDialog : LiveData<DatePickerDialog> = _datePickerDialog

    private val _username = MutableLiveData("")
    val username : LiveData<String> = _username

    private val _exercisesList = MutableLiveData(emptyList<Exercise>())
    val exercisesList : LiveData<List<Exercise>> = _exercisesList

    fun onUsernameChange() {
        _username.value = sharedPreferences.getString("username", "")
    }

    fun onDateChange() {
        _datePickerDialog.value = DatePickerDialog(
            appContext,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                _selectedDate.value = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(selectedCalendar.time)
                _dayOfWeek.value = selectedCalendar.get(Calendar.DAY_OF_WEEK)
            }, year, month, day
        )
        _datePickerDialog.value!!.show()
    }

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

    private fun getTrainingsList(username : String) {
        trainingsList = dbHelper.loadTrainings(appContext, username)!!
    }

    private fun saveTrainings(username: String) {
        dbHelper.saveTrainings(appContext, username, trainingsList)
    }

    private fun getExercisesForDay() {
        username.value?.let { getTrainingsList(it) }
        val list = mutableListOf<Exercise>()
        if (trainingsList.isEmpty()) {
            trainingsList = username.value?.let { generateTrainingsByUser(it) }!!
            saveTrainings(username.value!!)
        }
        for (training in trainingsList) {
            if (training.dayOfWeek == dayOfWeek.value)
                training.exercises.forEach { int -> list.add(dbHelper.getExerciseById(int)) }
        }
        _exercisesList.value = list
    }

    fun onShowExercisesSelected() {
        getExercisesForDay()
    }
}