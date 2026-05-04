package com.upb.intelliquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upb.intelliquiz.ui.screens.*
import com.upb.intelliquiz.ui.theme.IntelliQuizTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IntelliQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("inicio") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("inicio") {
            InicioScreen(
                onIniciarSesion = {
                    navController.navigate("inicio_sesion")
                },
                onRegistrate = {
                    // Por ahora navega al mismo login
                    navController.navigate("inicio_sesion")
                }
            )
        }

        composable("inicio_sesion") {
            InicioSesionScreen(
                onLoginSuccess = {
                    // Aquí irá al menú principal después del login
                    android.widget.Toast.makeText(
                        navController.context,
                        "Login exitoso!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                },
                onRegistrate = {
                    android.widget.Toast.makeText(
                        navController.context,
                        "Registro - Próximamente",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                },
                onOlvideContrasena = {
                    android.widget.Toast.makeText(
                        navController.context,
                        "Recuperar contraseña - Próximamente",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}