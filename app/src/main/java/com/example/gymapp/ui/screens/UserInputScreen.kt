package com.example.gymapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymapp.R
import com.example.gymapp.data.UserDataUiEvents
import com.example.gymapp.ui.ButtonComponent
import com.example.gymapp.ui.TextComponent
import com.example.gymapp.ui.TextFieldComponent
import com.example.gymapp.ui.TextFieldPasswordComponent
import com.example.gymapp.ui.TopBar
import com.example.gymapp.ui.UserInputViewModel

@Composable
fun UserInputScreen(
    userInputViewModel: UserInputViewModel = viewModel(),
    showWelcomeScreen: (valuesPair: Pair<String, String>) -> Unit,
    navController: NavController // Aquí pasamos el navController

) {
    val uiState = userInputViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        color = Color.Transparent // Asegúrate de que el color del Surface sea transparente
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen al inicio
            Image(
                painter = painterResource(id = R.drawable.img), // Reemplaza con tu imagen
                contentDescription = "Imagen de bienvenida",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Crop
            )

            TopBar("¡Bienvenido! 🏋️‍♂️ Prepárate para alcanzar tus metas")
            Spacer(modifier = Modifier.size(40.dp))

            TextComponent(textValue = "Usuario", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))

            TextFieldComponent(onTextChanged = {
                userInputViewModel.onEvent(UserDataUiEvents.UserNameEntered(it))
            })

            Spacer(modifier = Modifier.size(20.dp))

            TextComponent(textValue = "Contraseña", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))

            TextFieldPasswordComponent(onTextChanged = {
                userInputViewModel.onEvent(UserDataUiEvents.PasswordSelected(it))
            })

            Spacer(modifier = Modifier.size(40.dp))

            ButtonComponent(
                goToDetailScreen = {
                    userInputViewModel.validateUserCredentials(
                        onSuccess = {
                            showWelcomeScreen(
                                Pair(uiState.nameEntered, uiState.passwordSelected)
                            )
                        },
                        onFailure = {
                            Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            // Botón de registrarse
            Button(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                onClick = {
                    navController.navigate(Routes.Register_Screen)
                }
            ) {
                TextComponent(
                    textValue = "Registrarse",
                    textSize = 18.sp,
                    colorValue = Color.White
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            // Botón de cambiar de gimnasio
            Button(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                onClick = {  }
            ) {
                TextComponent(
                    textValue = "Cambiar de gimnasio",
                    textSize = 18.sp,
                    colorValue = Color.White
                )
            }
        }
    }
}
