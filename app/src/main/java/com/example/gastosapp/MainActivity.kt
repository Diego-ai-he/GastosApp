package com.example.gastosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastosapp.ui.theme.GastosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GastosAppTheme {
                PantallaPrincipal()
            }
        }
    }
}

@Composable
fun PantallaPrincipal() {
    // Variable para contar clicks (temporal, para probar)
    var contador by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espaciado superior
        Spacer(modifier = Modifier.height(32.dp))

        // Título
        Text(
            text = "Mis Gastos",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para agregar gasto
        Button(
            onClick = { contador++ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF27AE60)
            ),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(56.dp)
        ) {
            Text(
                text = "+ Agregar Gasto",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Texto temporal
        Text(
            text = "Aquí aparecerán tus gastos",
            fontSize = 16.sp,
            color = Color(0xFF7F8C8D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contador de prueba
        Text(
            text = "Has presionado el botón: $contador veces",
            fontSize = 14.sp,
            color = Color(0xFF34495E)
        )
    }
}