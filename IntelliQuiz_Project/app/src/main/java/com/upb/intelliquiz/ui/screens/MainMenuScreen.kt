package com.upb.intelliquiz.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TextGray
import com.upb.intelliquiz.ui.theme.TitleWhite

@Composable
fun MainMenuScreen(
    onLogout: () -> Unit,
    onPlayNow: () -> Unit,
    authViewModel: com.upb.intelliquiz.utils.AuthViewModel
) {
    val scrollState = rememberScrollState()
    var fullName by remember { androidx.compose.runtime.mutableStateOf("Nombre Completo") }
    var trophies by rememberSaveable { mutableStateOf(0) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        authViewModel.getCurrentUserData { data ->
            val nombre = data?.get("nombreCompleto") as? String
            if (!nombre.isNullOrBlank()) {
                fullName = nombre
            }
        }
    }

    // Fetch trophies initially and when app returns to foreground
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                authViewModel.getCurrentUserData { data ->
                    val trofeosVal = when (val v = data?.get("trofeos")) {
                        is Long -> v.toInt()
                        is Int -> v
                        is Double -> v.toInt()
                        else -> 0
                    }
                    trophies = trofeosVal
                    val nombre = data?.get("nombreCompleto") as? String
                    if (!nombre.isNullOrBlank()) fullName = nombre
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
                    .padding(18.dp)
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

                    Spacer(modifier = Modifier.height(26.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Bienvenido! $fullName",
                                color = BackgroundDark,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(32.dp))
                                    .background(ButtonPurple)
                                    .padding(horizontal = 28.dp, vertical = 14.dp)
                            ) {
                                Text(
                                    text = "🏆 $trophies",
                                    color = TitleWhite,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(TextGray.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PersonOutline,
                                contentDescription = "Avatar de usuario",
                                tint = BackgroundDark,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(34.dp))

                    Button(
                        onClick = onPlayNow,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .border(2.dp, BackgroundDark, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TitleWhite)
                    ) {
                        Text(
                            text = "Partida Rapida",
                            color = BackgroundDark,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "Ranking Semanal:",
                        color = BackgroundDark,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Rango",
                            modifier = Modifier.weight(0.9f),
                            color = BackgroundDark,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Amigo",
                            modifier = Modifier.weight(2f),
                            color = BackgroundDark,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Puntaje",
                            modifier = Modifier.weight(1.3f),
                            color = BackgroundDark,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = TextGray.copy(alpha = 0.5f), thickness = 1.dp)

                    val ranking = listOf(
                        "1" to "Rafael Pereira",
                        "2" to "Jotaro Kujo",
                        "3" to "Jonathan Joestar",
                        "4" to "Snake Joe"
                    )

                    ranking.forEach { (position, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = position,
                                modifier = Modifier.weight(0.9f),
                                color = BackgroundDark,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = name,
                                modifier = Modifier.weight(2f),
                                color = BackgroundDark,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1.3f)
                                    .clip(RoundedCornerShape(32.dp))
                                    .background(ButtonPurple)
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "\uD83C\uDFC6 743",
                                    color = TitleWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        HorizontalDivider(color = TextGray.copy(alpha = 0.45f), thickness = 1.dp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        BottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun BottomNavBar(
    modifier: Modifier = Modifier
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
            NavItem(
                label = "Home",
                icon = Icons.Outlined.Home,
                selected = true
            )
            NavItem(
                label = "Jugar",
                icon = Icons.Outlined.SportsEsports
            )
            NavItem(
                label = "Puntaje",
                icon = Icons.Outlined.EmojiEvents
            )
            NavItem(
                label = "Perfil",
                icon = Icons.Outlined.PersonOutline
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean = false
) {
    val containerColor = if (selected) TitleWhite else ButtonPurple
    val contentColor = if (selected) BackgroundDark else TitleWhite
    val iconSize = if (selected) 30.dp else 32.dp

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(containerColor)
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