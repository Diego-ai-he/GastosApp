package com.example.gastosapp.network

import retrofit2.http.GET
import retrofit2.http.Path

interface TipoCambioApiService {

    @GET("latest/{base}")
    suspend fun obtenerTasas(
        @Path("base") monedaBase: String = "USD"
    ): TipoCambioResponse
}