package com.example.gymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymapp.ui.screens.FunFactsNavigationGraph
import com.example.gymapp.ui.theme.GymAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymAppTheme {
                FunFactApp()
            }
        }
    }

    @Preview
    @Composable
    private fun FunFactApp() {
        FunFactsNavigationGraph()
    }


}

