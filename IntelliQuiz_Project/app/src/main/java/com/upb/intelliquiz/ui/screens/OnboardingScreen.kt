package com.upb.intelliquiz.ui.screens

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    var currentPage by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Cargar sonido al iniciar la pantalla
    LaunchedEffect(Unit) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
            mediaPlayer?.setVolume(0.7f, 0.7f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Liberar sonido al salir
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    val onboardingPages = listOf(
        OnboardingPage(
            icon = R.drawable.onboarding_icon_1,
            title = "Pon a Prueba Tu Mente",
            description = "Elige entre Historia, Ciencia, Matemáticas, Inglés o modo Aleatorio. Cada categoría tiene preguntas únicas diseñadas para retarte"
        ),
        OnboardingPage(
            icon = R.drawable.onboarding_icon_2,
            title = "Reta a tus amigos",
            description = "Compite con amigos y otros jugadores. Compara puntuaciones y demuestra quién es el verdadero sabio"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) with
                            fadeOut(animationSpec = tween(500))
                },
                label = "pageTransition"
            ) { page ->
                Image(
                    painter = painterResource(id = onboardingPages[page].icon),
                    contentDescription = "Onboarding icon",
                    modifier = Modifier.size(250.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = onboardingPages[currentPage].title,
                color = TitleWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.animateContentSize()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = onboardingPages[currentPage].description,
                color = TextGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(onboardingPages.size) { index ->
                    Box(
                        modifier = Modifier
                            .width(if (currentPage == index) 40.dp else 24.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (currentPage == index) IndicatorActive
                                else IndicatorInactive
                            )
                            .animateContentSize()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(ButtonCircleDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                        contentDescription = "Next",
                        tint = TitleWhite,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(if (currentPage == 0) 0f else 0f)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(ButtonPurple)
                        .clickable {
                            // Reproducir sonido al hacer clic
                            try {
                                mediaPlayer?.let { mp ->
                                    if (!mp.isPlaying) {
                                        mp.start()
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            if (currentPage < onboardingPages.size - 1) {
                                currentPage++
                            } else {
                                onComplete()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continuar",
                        color = TitleWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    IntelliQuizTheme {
        OnboardingScreen(onComplete = {})
    }
}

data class OnboardingPage(
    val icon: Int,
    val title: String,
    val description: String
)