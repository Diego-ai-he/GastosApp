package com.example.gastosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gastosapp.repository.GastoRepository

class GastosViewModelFactory(
    private val repository: GastoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}