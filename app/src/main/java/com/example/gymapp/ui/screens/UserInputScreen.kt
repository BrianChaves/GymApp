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
import androidx.compose.ui.unit.sp
import com.example.gymapp.data.UserDataUiEvents
import com.example.gymapp.ui.ButtonComponent
import com.example.gymapp.ui.TextComponent
import com.example.gymapp.ui.TextFieldComponent
import com.example.gymapp.ui.TextFieldPasswordComponent
import com.example.gymapp.ui.TopBar
import com.example.gymapp.ui.UserInputViewModel

@Composable
fun UserInputScreen(
    userInputViewModel: UserInputViewModel,
    showWelcomeScreen: (valuesPair: Pair<String, String>) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {

            // Barra superior de la pantalla
            TopBar("Bienvenidos a la aplicacion del Gym \uD83D\uDE0A")
            Spacer(modifier = Modifier.size(20.dp))

            // Texto de introducción
            TextComponent(textValue = "Usuario", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))

            TextFieldComponent(onTextChanged = {
                userInputViewModel.onEvent(
                    UserDataUiEvents.UserNameEntered(it)
                )
            })

            Spacer(modifier = Modifier.size(20.dp))

            TextComponent(textValue = "Contreña", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))

            TextFieldPasswordComponent(onTextChanged = {
                userInputViewModel.onEvent(
                    UserDataUiEvents.PasswordSelected(it)
                )
            })

            Spacer(modifier = Modifier.weight(1f))

            // Botón para avanzar a la pantalla de bienvenida si los datos son válidos
            if (userInputViewModel.isValidState()) {
                ButtonComponent(
                    goToDetailScreen = {
                        println("Los datos vienen")
                        showWelcomeScreen(
                            Pair(
                                userInputViewModel.uiState.value.nameEntered,
                                userInputViewModel.uiState.value.passwordSelected,
                            )
                        )
                    }
                )
            }


        }


    }


}
/*
@Preview
@Composable
fun UserInputScreenPreview(){
    UserInputScreen(UserInputViewModel())
}*/