package com.example.gymapp.data

data class Exercise(
    var exerciseId: Int,
    var exerciseName: String,
    var type: String
)

data class Training(
    var trainingId: Int,
    // String of integer for a day of week (1 to 7), starting Sunday = 1, ending Saturday = 7
    var dayOfWeek: Int,
    var username: String,
    //List of 20 integers, each is a different exerciseId
    var exercises: List<Int>
)

data class User(
    var userId: Int,
    var name: String,
    var username: String,
    var password: String
)
