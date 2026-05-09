package com.upb.intelliquiz.ui.screens

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonCircleDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TextGray
import com.upb.intelliquiz.ui.theme.TitleWhite

/**
 * Pantalla de fin de partida. Recibe los datos por parámetros (no hardcodeados)
 * y muestra el resumen completo con animación de entrada y dos acciones:
 * volver al home o reintentar la categoría jugada.
 */
@Composable
fun GameOverScreen(
    category: String,
    puntaje: Int,
    aciertos: Int,
    incorrectas: Int,
    tiempoSegundos: Int,
    racha: Int,
    porcentaje: Int,
    onVolverHome: () -> Unit,
    onIntentarOtraVez: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
            mediaPlayer?.setVolume(0.7f, 0.7f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        visible = true
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    fun playClick() {
        try {
            mediaPlayer?.let { mp ->
                if (!mp.isPlaying) mp.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val porcentajeSafe = porcentaje.coerceIn(0, 100)
    val tiempoFormateado = formatSeconds(tiempoSegundos)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(start = 12.dp, top = 48.dp, end = 12.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(450)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(450)) +
                        slideInVertically(
                            initialOffsetY = { it / 6 },
                            animationSpec = tween(450)
                        )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(TitleWhite)
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TitleWhite),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Logo IntelliQuiz",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = "IntelliQuiz",
                                color = BackgroundDark,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🎮",
                                fontSize = 36.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Game Over!",
                                color = BackgroundDark,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Categoría: $category",
                            color = TextGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(BackgroundDark)
                                .padding(vertical = 22.dp, horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⭐ Puntaje Final ⭐",
                                    color = TitleWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = puntaje.toString(),
                                    color = TitleWhite,
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "puntos",
                                    color = TextGray,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        StatRow(label = "✅ Aciertos", value = aciertos.toString())
                        StatRow(label = "❌ Incorrectas", value = incorrectas.toString())
                        StatRow(label = "⏱️ Tiempo", value = tiempoFormateado)
                        StatRow(label = "🔥 Racha máxima", value = racha.toString())

                        Spacer(modifier = Modifier.height(22.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Precisión",
                                color = BackgroundDark,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$porcentajeSafe%",
                                color = ButtonPurple,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = porcentajeSafe / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = ButtonPurple,
                            trackColor = TextGray.copy(alpha = 0.2f)
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = {
                                playClick()
                                onIntentarOtraVez()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
                        ) {
                            Text(
                                text = "Intentar Otra Vez",
                                color = TitleWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                playClick()
                                onVolverHome()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(2.dp, ButtonCircleDark, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TitleWhite)
                        ) {
                            Text(
                                text = "Volver Home",
                                color = BackgroundDark,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (porcentajeSafe >= 70) "¡Excelente trabajo! 🎉"
                else if (porcentajeSafe >= 40) "Sigue practicando 💪"
                else "No te rindas, ¡puedes mejorar! 🚀",
                color = TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = BackgroundDark,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = ButtonPurple,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatSeconds(totalSeconds: Int): String {
    val safe = totalSeconds.coerceAtLeast(0)
    val minutes = safe / 60
    val seconds = safe % 60
    return "%02d:%02d".format(minutes, seconds)
}
