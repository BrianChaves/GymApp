package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.ui.TopBar

@Composable
fun WelcomeScreen(username: String?, passwordSelected: String?, navController: NavController) {


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {

            TopBar(value = "Bienvenido $username \uD83D\uDE0A")

            Spacer(modifier = Modifier.size(20.dp))
            Button(onClick = { navController.navigate(Routes.Trainings_Screen) }) {
                Text("Pantalla Entrenamientos")
            }
            Spacer(modifier = Modifier.size(10.dp))
            Button(onClick = { navController.navigate(Routes.PRs_Screen) }) {
                Text("Pantalla de PRs")
            }
            Spacer(modifier = Modifier.size(10.dp))
            Button(onClick = { navController.navigate(Routes.Weight_Calculator_Screen) }) {
                Text("Pantalla Calculadora de Pesos")
            }


        }

    }

}

