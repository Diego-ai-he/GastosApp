package com.example.gastosapp

data class Gasto(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val fecha: String,
    val fotoRecibo: String? = null,
    val ubicacion: String? = null
)