package com.example.gastosapp.network

data class TipoCambioResponse(
    val conversion_rates: Map<String, Double>
)

data class ConversionResult(
    val moneda: String,
    val tasaCambio: Double,
    val montoOriginal: Double,
    val montoConvertido: Double
)