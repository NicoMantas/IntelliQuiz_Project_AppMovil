package com.upb.intelliquiz

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.upb.intelliquiz.ui.screens.*
import com.upb.intelliquiz.ui.theme.IntelliQuizTheme
import com.upb.intelliquiz.utils.AuthViewModel
import com.upb.intelliquiz.utils.AuthViewModelFactory
import com.upb.intelliquiz.utils.AuthState
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntelliQuizTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )

    // Observar el estado de autenticación
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Determinar la ruta inicial basada en autenticación
    val startDestination = remember(authState) {
        when (authState) {
            is AuthState.Authenticated -> "main_menu"
            else -> "splash"
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("inicio") {
                        popUpTo("onboarding") { inclusive = true }
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
                    navController.navigate("registro")
                }
            )
        }

        composable("inicio_sesion") {
            InicioSesionScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    // Limpiar todo el historial y navegar al menú principal
                    navController.navigate("main_menu") {
                        popUpTo("inicio") { inclusive = true }
                        popUpTo("inicio_sesion") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegistrate = {
                    navController.navigate("registro")
                },
                authViewModel = authViewModel
            )
        }

        composable("registro") {
            RegistroScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onRegistroSuccess = {
                    // Después del registro exitoso, ir al login
                    navController.navigate("inicio_sesion") {
                        popUpTo("registro") { inclusive = true }
                    }
                },
                onIniciarSesion = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }

        composable("main_menu") {
            MainMenuScreen(
                onLogout = {
                    authViewModel.logout()
                    // Navegar al splash después de cerrar sesión
                    navController.navigate("splash") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                },
                onPlayNow = {
                    navController.navigate("categorias_juegos")
                },
                authViewModel = authViewModel
            )
        }

        composable("categorias_juegos") {
            CategoriasJuegosScreen(
                onBack = {
                    navController.popBackStack()
                },
                onCategorySelected = { categoryName ->
                    navController.navigate("juego/${Uri.encode(categoryName)}")
                }
            )
        }

        composable(
            route = "juego/{category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Categoria"
            JuegoScreen(
                category = category,
                onBack = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }
    }
}