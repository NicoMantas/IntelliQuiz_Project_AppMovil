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

    // Helpers de navegación reutilizables para el bottom nav inferior
    val navigateToHome: () -> Unit = {
        navController.navigate("main_menu") {
            popUpTo("main_menu") { inclusive = false }
            launchSingleTop = true
        }
    }
    val navigateToCategorias: () -> Unit = {
        navController.navigate("categorias_juegos") {
            launchSingleTop = true
        }
    }
    val navigateToPuntaje: () -> Unit = {
        navController.navigate("puntaje") {
            launchSingleTop = true
        }
    }
    val navigateToPerfil: () -> Unit = {
        navController.navigate("perfil") {
            launchSingleTop = true
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
                onPlayNow = navigateToCategorias,
                onPuntajeClick = navigateToPuntaje,
                onPerfilClick = navigateToPerfil,
                authViewModel = authViewModel
            )
        }

        composable("categorias_juegos") {
            CategoriasJuegosScreen(
                onBack = {
                    if (!navController.popBackStack("main_menu", inclusive = false)) {
                        navigateToHome()
                    }
                },
                onCategorySelected = { categoryName ->
                    navController.navigate("juego/${Uri.encode(categoryName)}")
                },
                onPuntajeClick = navigateToPuntaje,
                onPerfilClick = navigateToPerfil
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
                onHomeClick = navigateToHome,
                onPuntajeClick = navigateToPuntaje,
                onPerfilClick = navigateToPerfil,
                onGameOver = { puntaje, aciertos, incorrectas, tiempo, racha ->
                    val total = (aciertos + incorrectas).coerceAtLeast(1)
                    val porcentaje = ((aciertos.toDouble() / total) * 100).toInt()
                    val route = "game_over/${Uri.encode(category)}/$puntaje/$aciertos/$incorrectas/$tiempo/$racha/$porcentaje"
                    navController.navigate(route) {
                        popUpTo("juego/{category}") { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(
            route = "game_over/{category}/{puntaje}/{aciertos}/{incorrectas}/{tiempo}/{racha}/{porcentaje}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("puntaje") { type = NavType.IntType },
                navArgument("aciertos") { type = NavType.IntType },
                navArgument("incorrectas") { type = NavType.IntType },
                navArgument("tiempo") { type = NavType.IntType },
                navArgument("racha") { type = NavType.IntType },
                navArgument("porcentaje") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            val category = args?.getString("category") ?: "Aleatorio"
            GameOverScreen(
                category = category,
                puntaje = args?.getInt("puntaje") ?: 0,
                aciertos = args?.getInt("aciertos") ?: 0,
                incorrectas = args?.getInt("incorrectas") ?: 0,
                tiempoSegundos = args?.getInt("tiempo") ?: 0,
                racha = args?.getInt("racha") ?: 0,
                porcentaje = args?.getInt("porcentaje") ?: 0,
                onVolverHome = {
                    navController.navigate("main_menu") {
                        popUpTo("main_menu") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onIntentarOtraVez = {
                    navController.navigate("juego/${Uri.encode(category)}") {
                        popUpTo("main_menu") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("puntaje") {
            PuntajeScreen(
                onHomeClick = navigateToHome,
                onJugarClick = navigateToCategorias,
                onPerfilClick = navigateToPerfil,
                authViewModel = authViewModel
            )
        }

        composable("perfil") {
            PerfilScreen(
                onHomeClick = navigateToHome,
                onJugarClick = navigateToCategorias,
                onPuntajeClick = navigateToPuntaje,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("splash") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
    }
}
