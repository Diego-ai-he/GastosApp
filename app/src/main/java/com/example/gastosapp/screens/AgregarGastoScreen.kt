package com.example.gastosapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastosapp.model.Gasto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarGastoScreen(
    onGastoGuardado: (Gasto) -> Unit,
    onVolver: () -> Unit
) {
    // Estados para cada campo del formulario
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Comida") }

    // Lista de categorías
    val categorias = listOf("Comida", "Transporte", "Entretenimiento", "Salud", "Otros")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Gasto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Validación simple
                    if (descripcion.isNotBlank() && monto.isNotBlank()) {
                        val montoDouble = monto.toDoubleOrNull() ?: 0.0
                        if (montoDouble > 0) {
                            val nuevoGasto = Gasto(
                                id = (1..10000).random(), // ID temporal
                                descripcion = descripcion,
                                monto = montoDouble,
                                categoria = categoriaSeleccionada,
                                fecha = "2025-10-18" // Fecha temporal
                            )
                            onGastoGuardado(nuevoGasto)
                        }
                    }
                },
                containerColor = Color(0xFF27AE60)
            ) {
                Icon(Icons.Filled.Check, "Guardar", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                placeholder = { Text("Ej: Almuerzo en restaurante") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo monto
            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                leadingIcon = {
                    Text("$", fontSize = 20.sp, modifier = Modifier.padding(start = 12.dp))
                }
            )

            // Selector de categoría
            Text(
                text = "Categoría",
                fontSize = 16.sp,
                color = Color(0xFF2C3E50)
            )

            categorias.forEach { categoria ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { categoriaSeleccionada = categoria }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = categoria,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}