package com.example.gymapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymapp.AdminOpenHelper
import com.example.gymapp.ui.TextComponent
import com.example.gymapp.ui.TextFieldComponent
import com.example.gymapp.ui.TextFieldPasswordComponent

import androidx.navigation.NavController

@Composable
fun UserRegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val adminOpenHelper = AdminOpenHelper.getInstance(context)

    // Usar remember para manejar el estado de los campos de texto
    val nameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        color = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            TextComponent(textValue = "Registro de Usuario", textSize = 24.sp)
            Spacer(modifier = Modifier.size(40.dp))

            // Campo para ingresar el nombre de usuario
            TextComponent(textValue = "Nombre", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))
            TextFieldComponent(onTextChanged = { nameState.value = it })

            Spacer(modifier = Modifier.size(20.dp))

            // Campo para ingresar la contraseña
            TextComponent(textValue = "Contraseña", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))
            TextFieldPasswordComponent(onTextChanged = { passwordState.value = it })

            Spacer(modifier = Modifier.size(20.dp))

            // Campo para repetir la contraseña
            TextComponent(textValue = "Repetir Contraseña", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))
            TextFieldPasswordComponent(onTextChanged = { confirmPasswordState.value = it })

            Spacer(modifier = Modifier.size(40.dp))

            // Botón de registrar usuario
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Validar que las contraseñas coincidan
                    if (passwordState.value != confirmPasswordState.value) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    } else {
                        // Intentar registrar al usuario
                        val success = adminOpenHelper.registerUser(nameState.value, passwordState.value)
                        if (success) {
                            Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                            // Navegar hacia la pantalla de inicio de sesión o la pantalla anterior
                            navController.popBackStack() // Esto lleva al usuario a la pantalla anterior
                        } else {
                            Toast.makeText(context, "Error en el registro: Usuario ya existe", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                TextComponent(
                    textValue = "Registrar",
                    textSize = 18.sp,
                    colorValue = Color.White
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.popBackStack() // Esto lleva al usuario a la pantalla anterior
                }
            ) {
                TextComponent(
                    textValue = "Regresar",
                    textSize = 18.sp,
                    colorValue = Color.White
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}
