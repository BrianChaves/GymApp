package com.example.gymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.ui.screens.FunFactsNavigationGraph
import com.example.gymapp.ui.theme.GymAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var gymRepository: GymRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "gymapp-db").build()
        gymRepository = GymRepository(db.userDao())

        enableEdgeToEdge()
        setContent {
            GymAppTheme {
                FunFactApp(gymRepository)
            }
        }
    }

    @Preview
    @Composable
    private fun FunFactApp(gymRepository : GymRepository) {
        FunFactsNavigationGraph(gymRepository)
    }


}

