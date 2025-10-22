package com.example.gastosapp.data

import androidx.room.*
import com.example.gastosapp.model.Gasto
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun obtenerTodosLosGastos(): Flow<List<Gasto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGasto(gasto: Gasto)

    @Delete
    suspend fun eliminarGasto(gasto: Gasto)

    @Query("SELECT * FROM gastos WHERE categoria = :categoria ORDER BY fecha DESC")
    fun obtenerGastosPorCategoria(categoria: String): Flow<List<Gasto>>

    @Query("SELECT SUM(monto) FROM gastos")
    fun obtenerTotalGastado(): Flow<Double?>
}