package com.example.gymapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.AdminOpenHelper
import com.example.gymapp.data.UserDataUiEvents
import com.example.gymapp.data.UserInputScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserInputViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UserInputScreenState())
    val uiState: StateFlow<UserInputScreenState> = _uiState

    private val dbHelper = AdminOpenHelper.getInstance(application)

    fun onEvent(event: UserDataUiEvents) {
        when (event) {
            is UserDataUiEvents.UserNameEntered -> _uiState.value = _uiState.value.copy(nameEntered = event.name)
            is UserDataUiEvents.PasswordSelected -> _uiState.value = _uiState.value.copy(passwordSelected = event.password)
        }
    }

    fun isValidState(): Boolean {
        return _uiState.value.nameEntered.isNotBlank() && _uiState.value.passwordSelected.isNotBlank()
    }

    fun validateUserCredentials(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val isValid = dbHelper.checkUser(_uiState.value.nameEntered, _uiState.value.passwordSelected)
            if (isValid) onSuccess() else onFailure()
        }
    }
}
