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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gastosapp.data.GastosDatabase
import com.example.gastosapp.repository.GastoRepository
import com.example.gastosapp.screens.AgregarGastoScreen
import com.example.gastosapp.ui.theme.GastosAppTheme
import com.example.gastosapp.viewmodel.GastosViewModel
import com.example.gastosapp.viewmodel.GastosViewModelFactory
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import android.net.Uri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar base de datos y repository
        val database = GastosDatabase.getDatabase(applicationContext)
        val repository = GastoRepository(database.gastoDao())
        val viewModelFactory = GastosViewModelFactory(repository)

        setContent {
            GastosAppTheme {
                AppNavigation(viewModelFactory = viewModelFactory)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModelFactory: GastosViewModelFactory) {
    val navController = rememberNavController()
    val viewModel: GastosViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = "principal"
    ) {
        composable("principal") {
            PantallaPrincipal(
                viewModel = viewModel,
                onAgregarClick = {
                    navController.navigate("agregar")
                }
            )
        }

        composable("agregar") {
            AgregarGastoScreen(
                onGastoGuardado = { nuevoGasto ->
                    viewModel.agregarGasto(nuevoGasto)
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
    viewModel: GastosViewModel,
    onAgregarClick: () -> Unit
) {
    // Observar datos desde el ViewModel
    val listaGastos by viewModel.listaGastos.collectAsState()
    val totalGastado by viewModel.totalGastado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
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

        Text(
            text = "Total gastado: $${"%.2f".format(totalGastado)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE74C3C),
            modifier = Modifier.padding(16.dp)
        )

        if (listaGastos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay gastos registrados\n¡Agrega tu primer gasto!",
                    fontSize = 16.sp,
                    color = Color(0xFF7F8C8D),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
fun TarjetaGasto(gasto: com.example.gastosapp.model.Gasto) {
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
            // Foto del recibo (si existe)
            gasto.fotoRecibo?.let { fotoUri ->
                androidx.compose.foundation.Image(
                    painter = coil.compose.rememberAsyncImagePainter(
                        model = android.net.Uri.parse(fotoUri)
                    ),
                    contentDescription = "Foto del recibo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            // Información del gasto
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

            // Monto
            Text(
                text = "$${"%.2f".format(gasto.monto)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE74C3C)
            )
        }
    }
}