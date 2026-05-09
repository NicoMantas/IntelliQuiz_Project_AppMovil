package com.upb.intelliquiz.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.components.IntelliQuizBottomNavBar
import com.upb.intelliquiz.ui.components.NavSection
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonCircleDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TextGray
import com.upb.intelliquiz.ui.theme.TitleWhite
import com.upb.intelliquiz.utils.AuthViewModel

private enum class PuntajeTab { MiPuntaje, MisAmigos }

private data class FriendRow(
    val uid: String,
    val nombre: String,
    val puntuacion: Long
)

private val CategoriasDisponibles = listOf(
    "Ciencia" to "🧪",
    "Matematicas" to "➗",
    "Ingles" to "🇬🇧",
    "Sociales" to "📜",
    "Aleatorio" to "🎲"
)

@Composable
fun PuntajeScreen(
    onHomeClick: () -> Unit,
    onJugarClick: () -> Unit,
    onPerfilClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(PuntajeTab.MiPuntaje) }

    var fullName by remember { mutableStateOf("Mi perfil") }
    var categoryScores by remember { mutableStateOf<Map<String, Long>>(emptyMap()) }
    var friends by remember { mutableStateOf<List<FriendRow>>(emptyList()) }
    var currentUid by remember { mutableStateOf<String?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentUid = authViewModel.getCurrentUser()?.uid

                authViewModel.getCurrentUserData { data ->
                    val nombre = data?.get("nombreCompleto") as? String
                    if (!nombre.isNullOrBlank()) fullName = nombre
                }

                authViewModel.getMyCategoryScores { scores ->
                    categoryScores = scores
                }

                authViewModel.getFriendsRanking { list ->
                    friends = list.mapNotNull { row ->
                        val uid = row["uid"] as? String ?: return@mapNotNull null
                        val nombre = (row["nombreCompleto"] as? String) ?: "Jugador"
                        val puntuacion = when (val v = row["puntuacionTotal"]) {
                            is Long -> v
                            is Int -> v.toLong()
                            is Double -> v.toLong()
                            else -> 0L
                        }
                        FriendRow(uid = uid, nombre = nombre, puntuacion = puntuacion)
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(start = 12.dp, top = 48.dp, end = 12.dp, bottom = 28.dp)
                .padding(bottom = 108.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(TitleWhite)
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
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

                    Spacer(modifier = Modifier.height(22.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(TextGray.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PersonOutline,
                                contentDescription = "Avatar",
                                tint = BackgroundDark,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = fullName,
                        color = BackgroundDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    PuntajeTabs(
                        selected = selectedTab,
                        onSelect = { selectedTab = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(280)) togetherWith
                                    fadeOut(animationSpec = tween(220)))
                        },
                        label = "puntaje_tab_transition"
                    ) { tab ->
                        when (tab) {
                            PuntajeTab.MiPuntaje -> MiPuntajeContent(scores = categoryScores)
                            PuntajeTab.MisAmigos -> MisAmigosContent(
                                friends = friends,
                                currentUid = currentUid
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        IntelliQuizBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selected = NavSection.Puntaje,
            onHomeClick = onHomeClick,
            onJugarClick = onJugarClick,
            onPerfilClick = onPerfilClick
        )
    }
}

@Composable
private fun PuntajeTabs(
    selected: PuntajeTab,
    onSelect: (PuntajeTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(TextGray.copy(alpha = 0.12f))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PuntajeTabButton(
            label = "Mi Puntaje",
            selected = selected == PuntajeTab.MiPuntaje,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(PuntajeTab.MiPuntaje) }
        )
        PuntajeTabButton(
            label = "Mis Amigos",
            selected = selected == PuntajeTab.MisAmigos,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(PuntajeTab.MisAmigos) }
        )
    }
}

@Composable
private fun PuntajeTabButton(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(if (selected) ButtonPurple else androidx.compose.ui.graphics.Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) TitleWhite else BackgroundDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MiPuntajeContent(scores: Map<String, Long>) {
    val rows = remember(scores) {
        CategoriasDisponibles
            .map { (label, emoji) ->
                Triple(label, emoji, scores[label.lowercase()] ?: 0L)
            }
            .sortedByDescending { it.third }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Rango",
                modifier = Modifier.weight(0.9f),
                color = BackgroundDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Categoría",
                modifier = Modifier.weight(2f),
                color = BackgroundDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Puntaje",
                modifier = Modifier.weight(1.4f),
                color = BackgroundDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = TextGray.copy(alpha = 0.5f), thickness = 1.dp)

        rows.forEachIndexed { index, (label, emoji, score) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (index + 1).toString(),
                    modifier = Modifier.weight(0.9f),
                    color = BackgroundDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.weight(2f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = emoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = BackgroundDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1.4f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(BackgroundDark)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🏆 $score",
                        color = TitleWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(color = TextGray.copy(alpha = 0.45f), thickness = 1.dp)
        }
    }
}

@Composable
private fun MisAmigosContent(
    friends: List<FriendRow>,
    currentUid: String?
) {
    val ordered = remember(friends) { friends.sortedByDescending { it.puntuacion } }
    val top1 = ordered.getOrNull(0)
    val top2 = ordered.getOrNull(1)
    val top3 = ordered.getOrNull(2)

    Column(modifier = Modifier.fillMaxWidth()) {
        if (top1 != null || top2 != null || top3 != null) {
            Podium(top1 = top1, top2 = top2, top3 = top3, currentUid = currentUid)
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = TextGray.copy(alpha = 0.4f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Rango",
                modifier = Modifier.weight(0.9f),
                color = BackgroundDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Amigo",
                modifier = Modifier.weight(2f),
                color = BackgroundDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Puntaje",
                modifier = Modifier.weight(1.4f),
                color = BackgroundDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = TextGray.copy(alpha = 0.5f), thickness = 1.dp)

        if (ordered.isEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Aún no tienes amigos en tu ranking.\nJuega partidas y compite con otros usuarios.",
                color = TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        ordered.forEachIndexed { index, friend ->
            val isMe = friend.uid == currentUid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (index + 1).toString(),
                    modifier = Modifier.weight(0.9f),
                    color = BackgroundDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isMe) "${friend.nombre} (Tú)" else friend.nombre,
                    modifier = Modifier.weight(2f),
                    color = BackgroundDark,
                    fontSize = 16.sp,
                    fontWeight = if (isMe) FontWeight.Bold else FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .weight(1.4f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(BackgroundDark)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🏆 ${friend.puntuacion}",
                        color = TitleWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (index < ordered.lastIndex) {
                HorizontalDivider(color = TextGray.copy(alpha = 0.45f), thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun Podium(
    top1: FriendRow?,
    top2: FriendRow?,
    top3: FriendRow?,
    currentUid: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PodiumColumn(
            position = 2,
            friend = top2,
            currentUid = currentUid,
            avatarSize = 64.dp,
            baseHeight = 70.dp,
            modifier = Modifier.weight(1f)
        )
        PodiumColumn(
            position = 1,
            friend = top1,
            currentUid = currentUid,
            avatarSize = 78.dp,
            baseHeight = 100.dp,
            modifier = Modifier.weight(1f)
        )
        PodiumColumn(
            position = 3,
            friend = top3,
            currentUid = currentUid,
            avatarSize = 60.dp,
            baseHeight = 50.dp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PodiumColumn(
    position: Int,
    friend: FriendRow?,
    currentUid: String?,
    avatarSize: androidx.compose.ui.unit.Dp,
    baseHeight: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    val isMe = friend?.uid == currentUid
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(TextGray.copy(alpha = 0.25f))
                .border(
                    width = if (position == 1) 3.dp else 2.dp,
                    color = if (position == 1) ButtonPurple else ButtonCircleDark,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.PersonOutline,
                contentDescription = "Avatar",
                tint = BackgroundDark,
                modifier = Modifier.size(avatarSize * 0.55f)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = friend?.nombre?.let { if (isMe) "Tú" else it.take(12) } ?: "—",
            color = BackgroundDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Text(
            text = "🏆 ${friend?.puntuacion ?: 0}",
            color = ButtonPurple,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(baseHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(if (position == 1) ButtonPurple else ButtonCircleDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = position.toString(),
                color = TitleWhite,
                fontSize = if (position == 1) 36.sp else 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
