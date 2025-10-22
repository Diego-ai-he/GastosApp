package com.example.gastosapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gastosapp.model.Gasto

class GastosViewModel : ViewModel() {

    // Estado: Lista de gastos
    var listaGastos by mutableStateOf(
        listOf(
            Gasto(1, "Almuerzo", 5000.0, "Comida", "2025-10-18"),
            Gasto(2, "Uber", 3500.0, "Transporte", "2025-10-18"),
            Gasto(3, "Netflix", 9990.0, "Entretenimiento", "2025-10-17")
        )
    )
        private set  // Solo el ViewModel puede modificar directamente

    // Función para agregar un gasto
    fun agregarGasto(gasto: Gasto) {
        listaGastos = listaGastos + gasto
    }

    // Función para obtener el total gastado
    fun obtenerTotalGastado(): Double {
        return listaGastos.sumOf { it.monto }
    }

    // Función para eliminar un gasto (la usaremos más adelante)
    fun eliminarGasto(gasto: Gasto) {
        listaGastos = listaGastos.filter { it.id != gasto.id }
    }

    // Función para obtener gastos por categoría
    fun obtenerGastosPorCategoria(categoria: String): List<Gasto> {
        return listaGastos.filter { it.categoria == categoria }
    }
}