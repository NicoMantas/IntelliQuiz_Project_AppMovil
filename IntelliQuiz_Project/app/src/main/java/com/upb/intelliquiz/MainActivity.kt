package com.upb.intelliquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upb.intelliquiz.ui.screens.*
import com.upb.intelliquiz.ui.theme.IntelliQuizTheme
import com.upb.intelliquiz.utils.AuthViewModel
import com.upb.intelliquiz.utils.AuthViewModelFactory

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

    NavHost(
        navController = navController,
        startDestination = "splash"
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
                    navController.navigate("main_menu") {
                        popUpTo("inicio") { inclusive = true }
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
            MainMenuScreen()
        }
    }
}