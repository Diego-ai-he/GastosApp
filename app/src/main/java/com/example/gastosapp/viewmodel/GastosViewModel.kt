package com.example.gastosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.model.Gasto
import com.example.gastosapp.repository.GastoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GastosViewModel(private val repository: GastoRepository) : ViewModel() {

    // Lista de gastos desde la base de datos
    val listaGastos: StateFlow<List<Gasto>> = repository.todosLosGastos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Total gastado desde la base de datos (maneja null)
    val totalGastado: StateFlow<Double> = repository.totalGastado
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    // Agregar un gasto
    fun agregarGasto(gasto: Gasto) {
        viewModelScope.launch {
            repository.insertarGasto(gasto)
        }
    }

    // Eliminar un gasto
    fun eliminarGasto(gasto: Gasto) {
        viewModelScope.launch {
            repository.eliminarGasto(gasto)
        }
    }

    // Obtener gastos por categoría
    fun obtenerGastosPorCategoria(categoria: String): StateFlow<List<Gasto>> {
        return repository.obtenerGastosPorCategoria(categoria)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
}