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
                    android.widget.Toast.makeText(
                        navController.context,
                        "Login exitoso!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                },
                onRegistrate = {
                    navController.navigate("registro")
                },
                onOlvideContrasena = {
                    navController.navigate("recuperar_metodo")  // ← Va a la pantalla de elegir
                }
            )
        }

        composable("recuperar_metodo") {
            RecuperarMetodoScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onMetodoCorreo = {
                    navController.navigate("recuperar_contraseña")  // ← Va a la pantalla de ingresar correo
                }
            )
        }

        composable("recuperar_contraseña") {
            RecuperarContraseñaScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onEnviarLink = {
                    android.widget.Toast.makeText(
                        navController.context,
                        "Link enviado a tu correo!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()  // ← Vuelve a InicioSesionScreen
                }
            )
        }

        composable("registro") {
            RegistroScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onRegistroSuccess = {
                    android.widget.Toast.makeText(
                        navController.context,
                        "Registro exitoso!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("inicio_sesion") {
                        popUpTo("registro") { inclusive = true }
                    }
                },
                onIniciarSesion = {
                    navController.popBackStack()
                }
            )
        }
        composable("recuperar_metodo") {
            RecuperarMetodoScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onMetodoCorreo = {
                    navController.navigate("recuperar_contraseña")
                }
            )
        }

        composable("recuperar_contraseña") {
            RecuperarContraseñaScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onEnviarLink = {
                    android.widget.Toast.makeText(
                        navController.context,
                        "Link enviado a tu correo!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
            )
        }
    }
}