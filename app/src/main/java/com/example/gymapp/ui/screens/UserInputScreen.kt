package com.example.gymapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    showWelcomeScreen: (valuesPair: Pair<String, String>) -> Unit
) {
    val uiState = userInputViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {

            TopBar("Bienvenidos a la aplicacion del Gym \uD83D\uDE0A")
            Spacer(modifier = Modifier.size(20.dp))

            TextComponent(textValue = "Usuario", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))

            TextFieldComponent(onTextChanged = {
                userInputViewModel.onEvent(UserDataUiEvents.UserNameEntered(it))
            })

            Spacer(modifier = Modifier.size(20.dp))

            TextComponent(textValue = "Contreña", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))

            TextFieldPasswordComponent(onTextChanged = {
                userInputViewModel.onEvent(UserDataUiEvents.PasswordSelected(it))
            })

            Spacer(modifier = Modifier.weight(1f))

            if (userInputViewModel.isValidState()) {
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
            }
        }
    }
}
