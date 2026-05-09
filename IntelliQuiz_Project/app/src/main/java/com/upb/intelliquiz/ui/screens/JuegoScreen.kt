package com.upb.intelliquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.ui.components.IntelliQuizBottomNavBar
import com.upb.intelliquiz.ui.components.NavSection
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TextGray
import com.upb.intelliquiz.ui.theme.TitleWhite
import com.upb.intelliquiz.utils.AuthViewModel

data class Pregunta(
    val id: Int,
    val texto: String,
    val opciones: List<String>,
    val respuestaCorrecta: Int
)

private const val SECONDS_PER_QUESTION = 10

fun getQuestionsByCategory(category: String): List<Pregunta> {
    return when (category.lowercase()) {
        "ciencia" -> listOf(
            Pregunta(1, "¿Cuál es el elemento químico más abundante en el universo?", listOf("Oxígeno", "Helio", "Nitrógeno", "Carbono"), 1),
            Pregunta(2, "¿Cuántos huesos tiene un adulto humano?", listOf("186", "206", "226", "246"), 1),
            Pregunta(3, "¿Qué tipo de radiación emite el sol?", listOf("Gamma", "Infrarroja", "Ultravioleta", "Todas las anteriores"), 3)
        )
        "matematicas" -> listOf(
            Pregunta(1, "¿Cuál es el resultado de 15 × 12?", listOf("170", "180", "190", "200"), 1),
            Pregunta(2, "¿Cuál es la raíz cuadrada de 144?", listOf("10", "12", "14", "16"), 1),
            Pregunta(3, "¿Cuántos grados tiene un ángulo recto?", listOf("45°", "90°", "180°", "360°"), 1)
        )
        "ingles" -> listOf(
            Pregunta(1, "¿Cuál es el pasado de 'go'?", listOf("Goed", "Going", "Went", "Gone"), 2),
            Pregunta(2, "¿Qué significa 'Happy'?", listOf("Triste", "Feliz", "Furioso", "Asustado"), 1),
            Pregunta(3, "¿Cuál es el plural de 'child'?", listOf("Childs", "Children", "Childes", "Childies"), 1)
        )
        "sociales" -> listOf(
            Pregunta(1, "¿En qué año terminó la Segunda Guerra Mundial?", listOf("1943", "1944", "1945", "1946"), 2),
            Pregunta(2, "¿Cuál es la capital de Colombia?", listOf("Medellín", "Cali", "Bogotá", "Barranquilla"), 2),
            Pregunta(3, "¿Cuántos continentes hay en el mundo?", listOf("5", "6", "7", "8"), 2)
        )
        else -> listOf(
            Pregunta(1, "¿Cuál es el planeta más grande del sistema solar?", listOf("Saturno", "Júpiter", "Neptuno", "Urano"), 1),
            Pregunta(2, "¿En qué año se inventó la bombilla?", listOf("1879", "1889", "1899", "1909"), 0),
            Pregunta(3, "¿Cuál es la capital de Francia?", listOf("Lyon", "París", "Marsella", "Toulouse"), 1)
        )
    }
}

/**
 * Pantalla principal de juego.
 *
 * @param onGameOver Callback al terminar la partida (puntos, aciertos, incorrectas, segundos, racha máxima)
 */
@Composable
fun JuegoScreen(
    category: String,
    onBack: () -> Unit,
    onHomeClick: () -> Unit = onBack,
    onPuntajeClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onGameOver: (puntaje: Int, aciertos: Int, incorrectas: Int, tiempo: Int, racha: Int) -> Unit = { _, _, _, _, _ -> onBack() },
    authViewModel: AuthViewModel? = null
) {
    val backgroundTone = when (category.lowercase()) {
        "ciencia" -> Color(0xFFB6ECE8)
        "matematicas" -> Color(0xFFD7E8FF)
        "ingles" -> Color(0xFFFFE4A8)
        "sociales" -> Color(0xFFE7DBC2)
        else -> Color(0xFFE3E3E8)
    }

    val preguntas = getQuestionsByCategory(category)
    val (currentIndex, setCurrentIndex) = remember { mutableStateOf(0) }
    val (selectedAnswer, setSelectedAnswer) = remember { mutableStateOf<Int?>(null) }
    val (score, setScore) = remember { mutableStateOf(0) }
    val (correctAnswersCount, setCorrectAnswersCount) = remember { mutableStateOf(0) }
    val (totalAnswered, setTotalAnswered) = remember { mutableStateOf(0) }
    val (totalTimeSeconds, setTotalTimeSeconds) = remember { mutableStateOf(0) }
    val (showFeedback, setShowFeedback) = remember { mutableStateOf(false) }
    val (lastCorrect, setLastCorrect) = remember { mutableStateOf(false) }
    val (streak, setStreak) = remember { mutableStateOf(0) }
    val (maxStreak, setMaxStreak) = remember { mutableStateOf(0) }
    val (lives, setLives) = remember { mutableStateOf(3) }
    val (secondsRemaining, setSecondsRemaining) = remember { mutableStateOf(SECONDS_PER_QUESTION) }
    val (isTimeout, setIsTimeout) = remember { mutableStateOf(false) }
    val (resultsPersisted, setResultsPersisted) = remember { mutableStateOf(false) }

    val preguntaActual = preguntas[currentIndex]
    val progress = (currentIndex + 1).toFloat() / preguntas.size.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = 48.dp, end = 12.dp, bottom = 28.dp)
                .padding(bottom = 108.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(TitleWhite)
                    .padding(18.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TitleWhite)
                                    .clickable(onClick = onBack),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = BackgroundDark,
                                    modifier = Modifier.size(28.dp)
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

                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(backgroundTone.copy(alpha = 0.9f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category.take(1),
                                color = BackgroundDark,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${currentIndex + 1}/${preguntas.size}",
                            color = BackgroundDark,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Puntos: $score",
                                color = ButtonPurple,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Vidas: ${"❤️".repeat(lives)}",
                                color = Color.Red,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "00:${secondsRemaining.toString().padStart(2,'0')}",
                                color = BackgroundDark,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ButtonPurple,
                        trackColor = TextGray.copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = preguntaActual.texto,
                        color = BackgroundDark,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    preguntaActual.opciones.forEachIndexed { index, opcion ->
                        AnswerButton(
                            text = opcion,
                            isSelected = selectedAnswer == index,
                            onClick = { setSelectedAnswer(index) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (selectedAnswer != null) {
                                val correcto = selectedAnswer == preguntaActual.respuestaCorrecta
                                val timeUsed = SECONDS_PER_QUESTION - secondsRemaining
                                setTotalTimeSeconds(totalTimeSeconds + timeUsed)
                                setTotalAnswered(totalAnswered + 1)
                                if (correcto) {
                                    setScore(score + 10)
                                    val newStreak = streak + 1
                                    setStreak(newStreak)
                                    if (newStreak > maxStreak) setMaxStreak(newStreak)
                                    setCorrectAnswersCount(correctAnswersCount + 1)
                                } else {
                                    setStreak(0)
                                    setLives(lives - 1)
                                }
                                setLastCorrect(correcto)
                                setIsTimeout(false)
                                setShowFeedback(true)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonPurple,
                            disabledContainerColor = TextGray.copy(alpha = 0.5f)
                        ),
                        enabled = selectedAnswer != null
                    ) {
                        Text(
                            text = if (currentIndex == preguntas.size - 1) "Finalizar" else "Siguiente",
                            color = TitleWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        IntelliQuizBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selected = NavSection.Jugar,
            secondLabel = "Modos",
            onHomeClick = onHomeClick,
            onJugarClick = {},
            onPuntajeClick = onPuntajeClick,
            onPerfilClick = onPerfilClick
        )

        if (showFeedback) {
            FeedbackDialog(
                correct = lastCorrect,
                pointsGained = if (lastCorrect) 10 else 0,
                streak = streak,
                timeout = isTimeout,
                onNext = {
                    setShowFeedback(false)
                    setIsTimeout(false)
                    setSecondsRemaining(SECONDS_PER_QUESTION)
                    if (currentIndex < preguntas.size - 1 && lives > 0) {
                        setCurrentIndex(currentIndex + 1)
                        setSelectedAnswer(null)
                    } else {
                        // Persistir resultado solo una vez por partida
                        if (!resultsPersisted) {
                            try {
                                authViewModel?.addGameResult(
                                    score = score.toLong(),
                                    correctAnswers = correctAnswersCount.toLong(),
                                    category = category
                                )
                            } catch (_: Exception) {
                            }
                            setResultsPersisted(true)
                        }
                        val incorrectas = (totalAnswered - correctAnswersCount).coerceAtLeast(0)
                        onGameOver(score, correctAnswersCount, incorrectas, totalTimeSeconds, maxStreak)
                    }
                }
            )
        }

        // Timer per question
        LaunchedEffect(currentIndex, showFeedback) {
            setSecondsRemaining(SECONDS_PER_QUESTION)
            if (!showFeedback) {
                var s = SECONDS_PER_QUESTION
                while (s > 0 && !showFeedback) {
                    delay(1000L)
                    s -= 1
                    setSecondsRemaining(s)
                }
                if (s <= 0 && !showFeedback) {
                    // Timeout: marcar incorrecta y registrar tiempo completo
                    setTotalTimeSeconds(totalTimeSeconds + SECONDS_PER_QUESTION)
                    setTotalAnswered(totalAnswered + 1)
                    setIsTimeout(true)
                    setLastCorrect(false)
                    setStreak(0)
                    setLives(lives - 1)
                    setShowFeedback(true)
                }
            }
        }
    }
}

@Composable
private fun FeedbackDialog(
    correct: Boolean,
    pointsGained: Int,
    streak: Int,
    timeout: Boolean = false,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(TitleWhite)
                .padding(22.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (correct) "✅ ¡CORRECTO!" else "❌ ¡INCORRECTO!",
                    color = BackgroundDark,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (correct) {
                    Text(
                        text = "Has ganado +$pointsGained puntos",
                        color = TextGray,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "🔥 Racha: $streak",
                        color = TextGray,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = if (timeout) "⏰ ¡Se acabó el tiempo!" else "💪 ¡Ups! Pero aprendiste algo nuevo",
                        color = TextGray,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
                ) {
                    Text(text = "Siguiente →", color = TitleWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AnswerButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) ButtonPurple else TitleWhite
            )
            .border(
                width = 2.dp,
                color = if (isSelected) ButtonPurple else TextGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = if (isSelected) TitleWhite else BackgroundDark,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
