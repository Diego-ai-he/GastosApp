package com.example.gastosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gastosapp.ui.theme.GastosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GastosAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Lista de gastos compartida entre pantallas
    var listaGastos by remember {
        mutableStateOf(listOf(
            Gasto(1, "Almuerzo", 5000.0, "Comida", "2025-10-18"),
            Gasto(2, "Uber", 3500.0, "Transporte", "2025-10-18"),
            Gasto(3, "Netflix", 9990.0, "Entretenimiento", "2025-10-17")
        ))
    }

    NavHost(
        navController = navController,
        startDestination = "principal"
    ) {
        // Pantalla Principal
        composable("principal") {
            PantallaPrincipal(
                listaGastos = listaGastos,
                onAgregarClick = {
                    navController.navigate("agregar")
                }
            )
        }

        // Pantalla Agregar Gasto
        composable("agregar") {
            AgregarGastoScreen(
                onGastoGuardado = { nuevoGasto ->
                    listaGastos = listaGastos + nuevoGasto
                    navController.popBackStack()
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun PantallaPrincipal(
    listaGastos: List<Gasto>,
    onAgregarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header con título y botón
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2C3E50))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mis Gastos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAgregarClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27AE60)
                ),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "+ Agregar Gasto",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }

        // Total gastado
        Text(
            text = "Total gastado: $${listaGastos.sumOf { it.monto }}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE74C3C),
            modifier = Modifier.padding(16.dp)
        )

        // Lista de gastos
        if (listaGastos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay gastos registrados",
                    fontSize = 16.sp,
                    color = Color(0xFF7F8C8D)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaGastos) { gasto ->
                    TarjetaGasto(gasto)
                }
            }
        }
    }
}

@Composable
fun TarjetaGasto(gasto: Gasto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gasto.descripcion,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = gasto.categoria,
                    fontSize = 14.sp,
                    color = Color(0xFF7F8C8D)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = gasto.fecha,
                    fontSize = 12.sp,
                    color = Color(0xFF95A5A6)
                )
            }

            Text(
                text = "$$${gasto.monto}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE74C3C)
            )
        }
    }
}