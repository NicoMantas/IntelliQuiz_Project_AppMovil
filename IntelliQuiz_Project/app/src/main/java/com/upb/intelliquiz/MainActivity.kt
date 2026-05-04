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
        // Pantalla de Splash
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Pantallas de Onboarding (2 pantallas)
        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("inicio") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Inicio (Bienvenida)
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

        // Pantalla de Inicio de Sesión
        composable("inicio_sesion") {
            InicioSesionScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
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
                }
            )
        }
    }
}