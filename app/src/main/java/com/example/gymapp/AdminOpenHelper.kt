package com.example.gymapp
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.gymapp.data.Exercise
import com.example.gymapp.data.Training
import com.example.gymapp.data.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AdminOpenHelper(
    context: Context
) : SQLiteOpenHelper(context, NOMBRE, null, VERSION) {

    companion object {
        private const val NOMBRE = "gymDB"
        private const val VERSION = 1

        @Volatile
        private var instance : AdminOpenHelper? = null

        fun getInstance(context: Context): AdminOpenHelper {
            return instance ?: synchronized(this) {
                instance ?: AdminOpenHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "usuario TEXT NOT NULL UNIQUE, " +
                "contrasena TEXT NOT NULL)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS ejercicios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "tipo TEXT NOT NULL, " +
                "record TEXT)")
        //        db?.execSQL("CREATE TABLE entrenamientos (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "dia INTEGER NOT NULL, " +
//                "usuario TEXT NOT NULL, " +
//                "lista_ejercicios TEXT, " +
//                "FOREIGN KEY (usuario) REFERENCES usuarios (usuario))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario_id INTEGER NOT NULL, " +
                "ejercicio_id INTEGER NOT NULL, " +
                "record TEXT, " +
                "FOREIGN KEY (usuario_id) REFERENCES usuarios(id), " +
                "FOREIGN KEY (ejercicio_id) REFERENCES ejercicios(id))")

        // Insertar clientes quemados
        val admin = ContentValues()
        admin.put("nombre", "Administrador")
        admin.put("usuario", "Brian_Chaves")
        admin.put("contrasena", "123") // Replace with a secure hashing mechanism
        db!!.insert("usuarios", null, admin)

//        loadUsers()
//        loadExercises()
    }
    fun getExerciseByName(exerciseName: String): Exercise? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ejercicios WHERE nombre = ?", arrayOf(exerciseName))
        var exercise: Exercise? = null
        if (cursor.moveToFirst()) {
            exercise = Exercise(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
        }
        cursor.close()
        db.close()
        return exercise
    }

    fun registerUser(userName: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("nombre", userName)
            put("usuario", userName)
            put("contrasena", password)
        }

        return try {
            db.insertOrThrow("usuarios", null, contentValues)
            db.close()
            true
        } catch (e: SQLException) {
            db.close()
            false
        }
    }

    fun updateExerciseRecord(exercise: Exercise): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("record", exercise.record)
        }
        val result = db.update("ejercicios", contentValues, "id = ?", arrayOf(exercise.exerciseId.toString()))
        db.close()
        return result
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO()
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?", arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    private fun insertUser(user : User) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

//        contentValues.put("id", user.userId)
        contentValues.put("nombre", user.name)
        contentValues.put("usuario", user.username)
        contentValues.put("contrasena", user.password)

        val int = db!!.insertWithOnConflict("usuarios", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return int
    }

    private fun insertExercise(exercise: Exercise) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        //contentValues.put("id", exercise.exerciseId)
        contentValues.put("nombre", exercise.exerciseName)
        contentValues.put("tipo", exercise.type)
        contentValues.put("record", exercise.record)


        val int = db!!.insertWithOnConflict("ejercicios", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return int
    }

    fun getExerciseById(exerciseId: Int) : Exercise {
        val db = this.readableDatabase
        val cursor  = db.rawQuery("SELECT * FROM ejercicios WHERE id = ?", arrayOf(exerciseId.toString()))
        val exercise = Exercise(0, "", "")

        try {
            if (cursor.moveToFirst()) {
                exercise.exerciseId = cursor.getInt(0)
                exercise.exerciseName = cursor.getString(1)
                exercise.type = cursor.getString(2)
            }
            cursor.close()
        } catch (_ : SQLException) {

        }
        db. close()

        return exercise
    }

    fun getAllExercises() : List<Exercise> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ejercicios", null)
        val list = mutableListOf<Exercise>()

        try {
            if (cursor.moveToFirst()) {
                do {
                    val exercise = Exercise(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                    list.add(exercise)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (_: SQLException) {

        }
        db.close()

        return list
    }



    fun saveTrainings(appContext: Context?, username: String, trainings : List<Training>) {
        val sharedPreferences: SharedPreferences = appContext!!.getSharedPreferences("trainings_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val jsonTrainings = gson.toJson(trainings)

        editor.putString(username, jsonTrainings)
        editor.apply()
    }

    fun loadTrainings(appContext: Context?, username: String): List<Training>? {
        val sharedPreferences: SharedPreferences = appContext!!.getSharedPreferences("trainings_prefs", Context.MODE_PRIVATE)
        val jsonTrainings = sharedPreferences.getString(username, null)

        return if (jsonTrainings != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Training>>() {}.type
            gson.fromJson(jsonTrainings, type)
        } else {
            emptyList()
        }
    }

    fun loadExercises() {
        val list = listOf(
            // Upper body exercises
            Exercise(1, "Push Up", "Upper body"),
            Exercise(2, "Bench Press", "Upper body"),
            Exercise(3, "Incline Bench Press", "Upper body"),
            Exercise(4, "Decline Bench Press", "Upper body"),
            Exercise(5, "Chest Fly", "Upper body"),
            Exercise(6, "Pec Deck", "Upper body"),
            Exercise(7, "Cable Cross Over", "Upper body"),
            Exercise(8, "Pull Up", "Upper body"),
            Exercise(9, "Lat Pulldown", "Upper body"),
            Exercise(10, "Seated Row", "Upper body"),
            Exercise(11, "Bent Over Row", "Upper body"),
            Exercise(12, "Single Arm Row", "Upper body"),
            Exercise(13, "Face Pull", "Upper body"),
            Exercise(14, "Bicep Curl", "Upper body"),
            Exercise(15, "Hammer Curl", "Upper body"),
            Exercise(16, "Preacher Curl", "Upper body"),
            Exercise(17, "Tricep Dip", "Upper body"),
            Exercise(18, "Tricep Extension", "Upper body"),
            Exercise(19, "Skull Crusher", "Upper body"),
            Exercise(20, "Overhead Tricep Extension", "Upper body"),

            // Lower body exercises
            Exercise(21, "Squat", "Lower body"),
            Exercise(22, "Front Squat", "Lower body"),
            Exercise(23, "Hack Squat", "Lower body"),
            Exercise(24, "Leg Press", "Lower body"),
            Exercise(25, "Lunges", "Lower body"),
            Exercise(26, "Step Up", "Lower body"),
            Exercise(27, "Deadlift", "Lower body"),
            Exercise(28, "Romanian Deadlift", "Lower body"),
            Exercise(29, "Sumo Deadlift", "Lower body"),
            Exercise(30, "Leg Curl", "Lower body"),
            Exercise(31, "Leg Extension", "Lower body"),
            Exercise(32, "Calf Raise", "Lower body"),
            Exercise(33, "Standing Calf Raise", "Lower body"),
            Exercise(34, "Seated Calf Raise", "Lower body"),

            // Core exercises
            Exercise(35, "Plank", "Core"),
            Exercise(36, "Side Plank", "Core"),
            Exercise(37, "Russian Twist", "Core"),
            Exercise(38, "Leg Raise", "Core"),
            Exercise(39, "Hanging Leg Raise", "Core"),
            Exercise(40, "Bicycle Crunch", "Core"),
            Exercise(41, "Sit Up", "Core"),
            Exercise(42, "Mountain Climber", "Core"),
            Exercise(43, "V-Up", "Core"),
            Exercise(44, "Toe Touch", "Core"),

            // Cardio exercises
            Exercise(45, "Jump Rope", "Cardio"),
            Exercise(46, "High Knees", "Cardio"),
            Exercise(47, "Burpee", "Cardio"),
            Exercise(48, "Box Jump", "Cardio"),
            Exercise(49, "Treadmill Run", "Cardio"),
            Exercise(50, "Cycling", "Cardio"),
            Exercise(51, "Rowing Machine", "Cardio"),
            Exercise(52, "Elliptical Trainer", "Cardio"),

            // Full body exercises
            Exercise(53, "Kettlebell Swing", "Full Body"),
            Exercise(54, "Clean and Jerk", "Full Body"),
            Exercise(55, "Snatch", "Full Body"),
            Exercise(56, "Turkish Get-Up", "Full Body"),
            Exercise(57, "Bear Crawl", "Full Body"),
            Exercise(58, "Battle Ropes", "Full Body"),
            Exercise(59, "Medicine Ball Slam", "Full Body"),
            Exercise(60, "Farmer's Walk", "Full Body"),

            Exercise(61, "Arnold Press", "Strength"),
            Exercise(62, "T-Bar Row", "Strength"),
            Exercise(63, "Landmine Press", "Strength"),
            Exercise(64, "Glute Bridge", "Strength"),
            Exercise(65, "Hip Thrust", "Strength"),
            Exercise(66, "Oblique Crunch", "Core"),
            Exercise(67, "Windshield Wipers", "Core"),
            Exercise(68, "Jump Squat", "Strength"),
            Exercise(69, "Sled Push", "Strength"),
            Exercise(70, "Plyo Push-Up", "Strength")

        )
        for(exercise in list) {
            val int = insertExercise(exercise)
            Log.i("FunFactApp", "Insercion Ejercicio(${exercise.exerciseName}): $int")
        }
    }

    fun loadUsers() {
        val list = listOf(
            User(1, "John Doe", "johndoe", "password123"),
            User(2, "Jane Smith", "janesmith", "password123"),
            User(3, "Mike Johnson", "mikejohnson", "password123"),
            User(4, "Emily Davis", "emilydavis", "password123"),
            User(5, "Chris Brown", "chrisbrown", "password123"),
            User(6, "Patricia Taylor", "patriciataylor", "password123"),
            User(7, "Robert Miller", "robertmiller", "password123"),
            User(8, "Linda Wilson", "lindawilson", "password123"),
            User(9, "James Moore", "jamesmoore", "password123"),
            User(10, "Barbara Clark", "barbaraclark", "password123"),
            User(11, "William Lewis", "williamlewis", "password123"),
            User(12, "Elizabeth Walker", "elizabethwalker", "password123"),
            User(13, "David Hall", "davidhall", "password123"),
            User(14, "Jennifer Allen", "jenniferallen", "password123"),
            User(15, "Michael Young", "michaelyoung", "password123"),
            User(16, "Linda King", "lindaking", "password123"),
            User(17, "Daniel Wright", "danielwright", "password123"),
            User(18, "Karen Lopez", "karenlopez", "password123"),
            User(19, "Joseph Hill", "josephhill", "password123"),
            User(20, "Susan Scott", "susanscott", "password123")
        )
        for(user in list) {
            val int = insertUser(user)
            Log.i("FunFactApp", "Insercion Usuario(${user.username}): $int")
        }
    }
}