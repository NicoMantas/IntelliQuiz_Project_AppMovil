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
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.SportsEsports
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TextGray
import com.upb.intelliquiz.ui.theme.TitleWhite

data class Pregunta(
    val id: Int,
    val texto: String,
    val opciones: List<String>,
    val respuestaCorrecta: Int
)

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

@Composable
fun JuegoScreen(
    category: String,
    onBack: () -> Unit
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
    val (showFeedback, setShowFeedback) = remember { mutableStateOf(false) }
    val (lastCorrect, setLastCorrect) = remember { mutableStateOf(false) }
    val (streak, setStreak) = remember { mutableStateOf(0) }
    val (lives, setLives) = remember { mutableStateOf(3) }
    val (secondsRemaining, setSecondsRemaining) = remember { mutableStateOf(10) }
    val (isTimeout, setIsTimeout) = remember { mutableStateOf(false) }

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
                                if (correcto) {
                                    setScore(score + 10)
                                    setStreak(streak + 1)
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

        JuegoBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedLabel = "Modos",
            onHomeClick = onBack
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
                    setSecondsRemaining(10)
                    if (currentIndex < preguntas.size - 1 && lives > 0) {
                        setCurrentIndex(currentIndex + 1)
                        setSelectedAnswer(null)
                    } else {
                        onBack()
                    }
                }
            )
        }

        // Timer per question
        LaunchedEffect(currentIndex, showFeedback) {
            setSecondsRemaining(10)
            if (!showFeedback) {
                var s = 10
                while (s > 0 && !showFeedback) {
                    delay(1000L)
                    s -= 1
                    setSecondsRemaining(s)
                }
                if (s <= 0 && !showFeedback) {
                    // timeout: mark incorrect
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

@Composable
private fun JuegoBottomNavBar(
    modifier: Modifier = Modifier,
    selectedLabel: String,
    onHomeClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ButtonPurple)
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            JuegoNavItem(
                label = "Home",
                icon = Icons.Outlined.Home,
                selected = selectedLabel == "Home",
                onClick = onHomeClick
            )
            JuegoNavItem(
                label = "Modos",
                icon = Icons.Outlined.SportsEsports,
                selected = selectedLabel == "Modos"
            )
            JuegoNavItem(
                label = "Puntaje",
                icon = Icons.Outlined.EmojiEvents,
                selected = selectedLabel == "Puntaje"
            )
            JuegoNavItem(
                label = "Perfil",
                icon = Icons.Outlined.PersonOutline,
                selected = selectedLabel == "Perfil"
            )
        }
    }
}

@Composable
private fun JuegoNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val containerColor = if (selected) TitleWhite else ButtonPurple
    val contentColor = if (selected) BackgroundDark else TitleWhite
    val iconSize = if (selected) 30.dp else 32.dp

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(containerColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(
                horizontal = if (selected) 22.dp else 0.dp,
                vertical = if (selected) 12.dp else 0.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(iconSize)
        )

        if (selected) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = contentColor,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
