package com.example.gymapp.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.gymapp.data.UserDataUiEvents
import com.example.gymapp.data.UserInputScreenState


class UserInputViewModel : ViewModel() {

    companion object {
        const val TAG = "UserInputViewModel"
    }

    var uiState = mutableStateOf(UserInputScreenState())

    fun onEvent(events: UserDataUiEvents) {
        when (events) {
            is UserDataUiEvents.UserNameEntered -> {
                uiState.value = uiState.value.copy(
                    nameEntered = events.name
                )


            }

            is UserDataUiEvents.PasswordSelected -> {
                uiState.value = uiState.value.copy(
                    passwordSelected = events.password
                )

            }
        }
    }

    fun isValidState(): Boolean {
        val allFieldsFilled =
            uiState.value.nameEntered.isNullOrEmpty().not() && uiState.value.passwordSelected.isNullOrEmpty().not()
        if (allFieldsFilled) {
            return true
        } else {
            return false
        }
    }
}



