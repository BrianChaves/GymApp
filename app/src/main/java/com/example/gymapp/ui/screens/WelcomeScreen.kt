package com.example.gymapp.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.ui.TopBar

fun saveLoginData(sharedPreferences: SharedPreferences, username: String) {
    val editor = sharedPreferences.edit()
    editor.putString("username", username)
    editor.apply()
}

@Composable
fun WelcomeScreen(username: String?, passwordSelected: String?, navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
    saveLoginData(sharedPreferences, username!!)

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween // Para mover el botón de cerrar sesión al final
        ) {

            Column(
                modifier = Modifier.weight(1f), // Para ocupar el espacio disponible
                verticalArrangement = Arrangement.Center
            ) {
                TopBar(value = "Bienvenido $username \uD83D\uDE0A")

                Spacer(modifier = Modifier.size(20.dp))

                Button(
                    onClick = { navController.navigate(Routes.Trainings_Screen) },
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    Icon(imageVector = Icons.Filled.FitnessCenter, contentDescription = "Entrenamientos")
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Pantalla Entrenamientos")
                }

                Spacer(modifier = Modifier.size(10.dp))

                Button(
                    onClick = { navController.navigate(Routes.Weight_Calculator_Screen) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(imageVector = Icons.Filled.History, contentDescription = "Calculadora de Pesos")
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Pantalla Calculadora de Pesos")
                }
            }

            Button(
                onClick = {
                    val editor = sharedPreferences.edit()
                    editor.remove("username")
                    editor.apply()
                    navController.navigate(Routes.User_Input_Screen)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "Cerrar Sesión")
                Spacer(modifier = Modifier.size(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}
