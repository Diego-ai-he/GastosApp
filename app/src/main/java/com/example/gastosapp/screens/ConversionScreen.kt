package com.example.gastosapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastosapp.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionScreen(onVolver: () -> Unit) {
    val scope = rememberCoroutineScope()

    var monto by remember { mutableStateOf("") }
    var monedaOrigen by remember { mutableStateOf("USD") }
    var monedaDestino by remember { mutableStateOf("CLP") }
    var resultado by remember { mutableStateOf<Double?>(null) }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val monedas = listOf(
        "USD" to "🇺🇸 Dólar",
        "EUR" to "🇪🇺 Euro",
        "CLP" to "🇨🇱 Peso Chileno",
        "ARS" to "🇦🇷 Peso Argentino",
        "BRL" to "🇧🇷 Real",
        "MXN" to "🇲🇽 Peso Mexicano",
        "GBP" to "🇬🇧 Libra"
    )

    fun convertir() {
        val montoDouble = monto.toDoubleOrNull()
        if (montoDouble == null || montoDouble <= 0) {
            error = "Ingresa un monto válido"
            return
        }

        scope.launch {
            cargando = true
            error = null
            resultado = null
            try {
                val response = RetrofitClient.tipoCambioApi.obtenerTasas(monedaOrigen)
                val tasa = response.conversion_rates[monedaDestino]

                if (tasa != null) {
                    resultado = montoDouble * tasa
                } else {
                    error = "Moneda no disponible"
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conversor de Monedas") },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "💱 Convierte tus gastos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // MONTO
            OutlinedTextField(
                value = monto,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        monto = it
                    }
                },
                label = { Text("Monto") },
                placeholder = { Text("100.00") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            // MONEDA ORIGEN
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFECF0F1)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Desde:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7F8C8D)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    monedas.forEach { (codigo, nombre) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = monedaOrigen == codigo,
                                onClick = { monedaOrigen = codigo }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("$nombre ($codigo)")
                        }
                    }
                }
            }

            // ICONO DE INTERCAMBIO
            Icon(
                Icons.Filled.SwapVert,
                "Convertir",
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF3498DB)
            )

            // MONEDA DESTINO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFECF0F1)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Hacia:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7F8C8D)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    monedas.forEach { (codigo, nombre) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = monedaDestino == codigo,
                                onClick = { monedaDestino = codigo }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("$nombre ($codigo)")
                        }
                    }
                }
            }

            // BOTÓN CONVERTIR
            Button(
                onClick = { convertir() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27AE60)
                ),
                enabled = !cargando && monto.isNotBlank()
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Convirtiendo...")
                } else {
                    Text("Convertir", fontSize = 18.sp)
                }
            }

            // RESULTADO
            resultado?.let { res ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF3498DB)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Resultado:",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$monedaOrigen $monto",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "=",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$monedaDestino ${"%.2f".format(res)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // ERROR
            error?.let { err ->
                Text(
                    text = "❌ $err",
                    color = Color(0xFFE74C3C),
                    fontSize = 14.sp
                )
            }

            // INFO
            Text(
                text = "📡 Datos de ExchangeRate-API",
                fontSize = 12.sp,
                color = Color(0xFF95A5A6)
            )
        }
    }
}