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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun CategoriasJuegosScreen(
    onBack: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val categories = listOf(
        CategoryUi("Ciencia", "🧪", Color(0xFFB6ECE8)),
        CategoryUi("Matematicas", "➗", Color(0xFFD7E8FF)),
        CategoryUi("Ingles", "🇬🇧", Color(0xFFFFE4A8)),
        CategoryUi("Sociales", "📜", Color(0xFFE7DBC2)),
        CategoryUi("Aleatorio", "🎲", Color(0xFFE3E3E8))
    )

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

                    Spacer(modifier = Modifier.height(70.dp))

                    Text(
                        text = "Categorias",
                        color = BackgroundDark,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    HorizontalDivider(color = BackgroundDark, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(40.dp))

                    categories.forEach { category ->
                        CategoryCard(
                            category = category,
                            onClick = { onCategorySelected(category.label) }
                        )
                        Spacer(modifier = Modifier.height(22.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        CategoriesBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedLabel = "Modos",
            onHomeClick = onBack
        )
    }
}

@Composable
private fun CategoryCard(
    category: CategoryUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(TitleWhite)
            .border(1.dp, TextGray.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(CircleShape)
                .background(category.tint),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.emoji,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = category.label,
            color = BackgroundDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CategoriesBottomNavBar(
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
            NavItem(
                label = "Home",
                icon = Icons.Outlined.Home,
                selected = selectedLabel == "Home",
                onClick = onHomeClick
            )
            NavItem(
                label = "Modos",
                icon = Icons.Outlined.SportsEsports,
                selected = selectedLabel == "Modos"
            )
            NavItem(
                label = "Puntaje",
                icon = Icons.Outlined.EmojiEvents,
                selected = selectedLabel == "Puntaje"
            )
            NavItem(
                label = "Perfil",
                icon = Icons.Outlined.PersonOutline,
                selected = selectedLabel == "Perfil"
            )
        }
    }
}

@Composable
private fun NavItem(
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

private data class CategoryUi(
    val label: String,
    val emoji: String,
    val tint: Color
)