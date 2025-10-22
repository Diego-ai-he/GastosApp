package com.example.gastosapp.repository

import com.example.gastosapp.data.GastoDao
import com.example.gastosapp.model.Gasto
import kotlinx.coroutines.flow.Flow

class GastoRepository(private val gastoDao: GastoDao) {

    // Obtener todos los gastos
    val todosLosGastos: Flow<List<Gasto>> = gastoDao.obtenerTodosLosGastos()

    // Obtener total gastado
    val totalGastado: Flow<Double?> = gastoDao.obtenerTotalGastado()

    // Insertar gasto
    suspend fun insertarGasto(gasto: Gasto) {
        gastoDao.insertarGasto(gasto)
    }

    // Eliminar gasto
    suspend fun eliminarGasto(gasto: Gasto) {
        gastoDao.eliminarGasto(gasto)
    }

    // Obtener gastos por categoría
    fun obtenerGastosPorCategoria(categoria: String): Flow<List<Gasto>> {
        return gastoDao.obtenerGastosPorCategoria(categoria)
    }
}
