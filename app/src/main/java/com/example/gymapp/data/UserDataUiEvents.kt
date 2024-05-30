package com.example.gymapp.data

//Maneja Eventos
sealed class UserDataUiEvents {
    data class UserNameEntered(val name: String) : UserDataUiEvents()
    data class PasswordSelected(val password: String) : UserDataUiEvents()

}