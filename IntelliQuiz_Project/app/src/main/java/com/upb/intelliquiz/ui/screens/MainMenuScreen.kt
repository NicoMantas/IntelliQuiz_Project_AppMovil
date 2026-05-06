package com.upb.intelliquiz.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.EmojiEvents
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TextGray
import com.upb.intelliquiz.ui.theme.TitleWhite

@Composable
fun MainMenuScreen(
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 28.dp)
                .padding(bottom = 108.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_page),
                    contentDescription = "Logo IntelliQuiz",
                    tint = TitleWhite,
                    modifier = Modifier.size(46.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "IntelliQuiz",
                    color = TitleWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Bienvenido! Nombre Completo",
                color = TitleWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .background(ButtonPurple)
                    .padding(horizontal = 28.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "\uD83C\uDFC6 743",
                    color = TitleWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(54.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TitleWhite)
            ) {
                Text(
                    text = "Partida Rapida",
                    color = BackgroundDark,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Ranking Semanal:",
                color = TitleWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Rango",
                    modifier = Modifier.weight(0.9f),
                    color = TitleWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Amigo",
                    modifier = Modifier.weight(2f),
                    color = TitleWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Puntaje",
                    modifier = Modifier.weight(1.3f),
                    color = TitleWhite,
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
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = position,
                        modifier = Modifier.weight(0.9f),
                        color = TitleWhite,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = name,
                        modifier = Modifier.weight(2f),
                        color = TitleWhite,
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

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
            ) {
                Text("Cerrar Sesión", color = TitleWhite)
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

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .background(containerColor)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )

        if (selected) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = contentColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}