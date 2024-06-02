package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gymapp.ui.BackButtonComponent
import com.example.gymapp.ui.TopBar

@Composable
fun PRsScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {

            TopBar(value = "Bienvenido a la pantalla de PRs\uD83D\uDE0A")

            Spacer(modifier = Modifier.size(20.dp))

            BackButtonComponent(navController = navController)

        }

    }
}