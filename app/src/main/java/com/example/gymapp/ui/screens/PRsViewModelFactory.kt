package com.example.gymapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PRsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PRsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PRsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
