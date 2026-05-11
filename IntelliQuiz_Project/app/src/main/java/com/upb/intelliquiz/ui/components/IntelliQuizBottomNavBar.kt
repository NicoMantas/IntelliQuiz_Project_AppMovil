package com.upb.intelliquiz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TitleWhite

/**
 * Secciones de navegación inferior compartidas por toda la app.
 * Mantiene exactamente el mismo estilo visual usado desde MainMenuScreen.
 */
enum class NavSection { Home, Jugar, Puntaje, Perfil }

/**
 * Barra de navegación inferior reutilizable.
 *
 * Centraliza el look & feel original (pill blanca para el ítem seleccionado,
 * fondo morado, ítems Home / Jugar(Modos) / Puntaje / Perfil).
 *
 * @param secondLabel Permite que el segundo ítem se muestre como "Jugar"
 *                    (en flujos de menú/perfil) o como "Modos" (dentro del
 *                    flujo de juego), conservando la convención existente.
 */
@Composable
fun IntelliQuizBottomNavBar(
    selected: NavSection,
    modifier: Modifier = Modifier,
    secondLabel: String = "Jugar",
    onHomeClick: () -> Unit = {},
    onJugarClick: () -> Unit = {},
    onPuntajeClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
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
            BottomNavItem(
                label = "Home",
                icon = Icons.Outlined.Home,
                selected = selected == NavSection.Home,
                onClick = onHomeClick
            )
            BottomNavItem(
                label = secondLabel,
                icon = Icons.Outlined.SportsEsports,
                selected = selected == NavSection.Jugar,
                onClick = onJugarClick
            )
            BottomNavItem(
                label = "Puntaje",
                icon = Icons.Outlined.EmojiEvents,
                selected = selected == NavSection.Puntaje,
                onClick = onPuntajeClick
            )
            BottomNavItem(
                label = "Perfil",
                icon = Icons.Outlined.PersonOutline,
                selected = selected == NavSection.Perfil,
                onClick = onPerfilClick
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) TitleWhite else ButtonPurple
    val contentColor = if (selected) BackgroundDark else TitleWhite
    val iconSize = if (selected) 30.dp else 32.dp

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(containerColor)
            .clickable(onClick = onClick)
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
