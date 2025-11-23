package com.example.gastosapp.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.gastosapp.model.Gasto
import com.example.gastosapp.rememberCameraLauncher
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Add


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarGastoScreen(
    onGastoGuardado: (Gasto) -> Unit,
    onVolver: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Comida") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    var descripcionError by remember { mutableStateOf<String?>(null) }
    var montoError by remember { mutableStateOf<String?>(null) }
    var mostrarErrores by remember { mutableStateOf(false) }

    val categorias = listOf("Comida", "Transporte", "Entretenimiento", "Salud", "Otros")

    // Camera launcher
    val (takePicture, hasCameraPermission) = rememberCameraLauncher { uri ->
        fotoUri = uri
    }

    fun validarFormulario(): Boolean {
        var esValido = true

        descripcionError = when {
            descripcion.isBlank() -> {
                esValido = false
                "La descripción es obligatoria"
            }
            descripcion.length < 3 -> {
                esValido = false
                "Mínimo 3 caracteres"
            }
            descripcion.length > 50 -> {
                esValido = false
                "Máximo 50 caracteres"
            }
            else -> null
        }

        val montoDouble = monto.toDoubleOrNull()
        montoError = when {
            monto.isBlank() -> {
                esValido = false
                "El monto es obligatorio"
            }
            montoDouble == null -> {
                esValido = false
                "Ingresa un número válido"
            }
            montoDouble <= 0 -> {
                esValido = false
                "El monto debe ser mayor a 0"
            }
            montoDouble > 10000000 -> {
                esValido = false
                "Monto demasiado alto"
            }
            else -> null
        }

        return esValido
    }

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
                    mostrarErrores = true
                    if (validarFormulario()) {
                        val nuevoGasto = Gasto(
                            id = 0,
                            descripcion = descripcion.trim(),
                            monto = monto.toDouble(),
                            categoria = categoriaSeleccionada,
                            fecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
                            fotoRecibo = fotoUri?.toString()
                        )
                        onGastoGuardado(nuevoGasto)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // CAMPO DESCRIPCIÓN
            Column {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                        if (mostrarErrores) validarFormulario()
                    },
                    label = { Text("Descripción *") },
                    placeholder = { Text("Ej: Almuerzo en restaurante") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = mostrarErrores && descripcionError != null,
                    trailingIcon = {
                        if (mostrarErrores) {
                            if (descripcionError != null) {
                                Icon(Icons.Filled.Close, "Error", tint = Color(0xFFE74C3C))
                            } else if (descripcion.isNotBlank()) {
                                Icon(Icons.Filled.Done, "Correcto", tint = Color(0xFF27AE60))
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (mostrarErrores && descripcionError != null)
                            Color(0xFFE74C3C) else Color(0xFF3498DB),
                        unfocusedBorderColor = if (mostrarErrores && descripcionError != null)
                            Color(0xFFE74C3C) else Color(0xFFBDC3C7)
                    )
                )

                if (mostrarErrores && descripcionError != null) {
                    Text(
                        text = descripcionError!!,
                        color = Color(0xFFE74C3C),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Text(
                    text = "${descripcion.length}/50",
                    fontSize = 12.sp,
                    color = Color(0xFF95A5A6),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            // CAMPO MONTO
            Column {
                OutlinedTextField(
                    value = monto,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            monto = it
                            if (mostrarErrores) validarFormulario()
                        }
                    },
                    label = { Text("Monto *") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = mostrarErrores && montoError != null,
                    leadingIcon = {
                        Text("$", fontSize = 20.sp, modifier = Modifier.padding(start = 12.dp))
                    },
                    trailingIcon = {
                        if (mostrarErrores) {
                            if (montoError != null) {
                                Icon(Icons.Filled.Close, "Error", tint = Color(0xFFE74C3C))
                            } else if (monto.isNotBlank()) {
                                Icon(Icons.Filled.Done, "Correcto", tint = Color(0xFF27AE60))
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (mostrarErrores && montoError != null)
                            Color(0xFFE74C3C) else Color(0xFF3498DB),
                        unfocusedBorderColor = if (mostrarErrores && montoError != null)
                            Color(0xFFE74C3C) else Color(0xFFBDC3C7)
                    )
                )

                if (mostrarErrores && montoError != null) {
                    Text(
                        text = montoError!!,
                        color = Color(0xFFE74C3C),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            // SELECTOR DE CATEGORÍA
            Column {
                Text(
                    text = "Categoría *",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )

                Spacer(modifier = Modifier.height(8.dp))

                categorias.forEach { categoria ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = categoriaSeleccionada == categoria,
                            onClick = { categoriaSeleccionada = categoria },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF3498DB)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = categoria, fontSize = 16.sp, color = Color(0xFF2C3E50))
                    }
                }
            }

            // SECCIÓN DE FOTO
            Column {
                Text(
                    text = "Foto del recibo (opcional)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { takePicture() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3498DB)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Add, "Tomar foto", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (fotoUri == null) "Tomar Foto" else "Cambiar Foto")
                }

                // Mostrar preview de la foto
                fotoUri?.let { uri ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Foto del recibo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )

                            // Botón para eliminar foto
                            IconButton(
                                onClick = { fotoUri = null },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    "Eliminar foto",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .border(2.dp, Color.White, RoundedCornerShape(50))
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Ayuda visual
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFECF0F1)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "ℹ️",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Completa todos los campos marcados con * para guardar el gasto",
                        fontSize = 12.sp,
                        color = Color(0xFF7F8C8D)
                    )
                }
            }
        }
    }
}