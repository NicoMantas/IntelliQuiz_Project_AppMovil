package com.upb.intelliquiz

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upb.intelliquiz.ui.screens.OnboardingScreen
import com.upb.intelliquiz.ui.screens.SplashScreen
import com.upb.intelliquiz.ui.theme.IntelliQuizTheme

class MainActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSound()

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

    private fun setupSound() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click)

            if (mediaPlayer != null) {
                mediaPlayer?.setVolume(0.7f, 0.7f)
                Log.d("MainActivity", "✅ Sonido cargado exitosamente")
            } else {
                Log.e("MainActivity", "❌ MediaPlayer.create() devolvió null")
                mediaPlayer = null
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "❌ Error cargando sonido: ${e.message}")
            mediaPlayer = null
        }
    }

    private fun playSound() {
        try {
            mediaPlayer?.let { mp ->
                if (!mp.isPlaying) {
                    mp.start()
                    Log.d("MainActivity", "🔊 Reproduciendo sonido")
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "❌ Error reproduciendo: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            Log.d("MainActivity", "🎵 Sonido liberado")
        } catch (e: Exception) {
            e.printStackTrace()
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
                        playSound() // Reproduce sonido al completar onboarding
                        android.widget.Toast.makeText(
                            navController.context,
                            "Onboarding completado. Aquí irá el menú principal",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}