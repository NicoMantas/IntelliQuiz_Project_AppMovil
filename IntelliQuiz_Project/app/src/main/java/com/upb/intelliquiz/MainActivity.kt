package com.upb.intelliquiz

import android.media.MediaPlayer
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
import com.upb.intelliquiz.ui.screens.OnboardingScreen
import com.upb.intelliquiz.ui.screens.SplashScreen
import com.upb.intelliquiz.ui.theme.IntelliQuiz_ProjectTheme
import com.upb.intelliquiz.utils.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesManager = PreferencesManager(this)
        setupSound()

        setContent {
            IntelliQuiz_ProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(preferencesManager, mediaPlayer)
                }
            }
        }
    }

    private fun setupSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click).apply {
            setVolume(0.5f, 0.5f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}

@Composable
fun AppNavigation(preferencesManager: PreferencesManager, mediaPlayer: MediaPlayer) {
    val navController = rememberNavController()
    var isFirstLaunch by remember {
        mutableStateOf(runBlocking { preferencesManager.isFirstLaunch() })
    }

    NavHost(
        navController = navController,
        startDestination = if (isFirstLaunch) "splash" else "main_menu"
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
                    runBlocking {
                        preferencesManager.setOnboardingCompleted()
                    }
                    navController.navigate("main_menu") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                mediaPlayer = mediaPlayer
            )
        }

        composable("main_menu") {
            // Aquí irá tu menú principal
            MainMenuScreen(mediaPlayer)
        }
    }
}