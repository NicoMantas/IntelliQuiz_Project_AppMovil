package com.upb.intelliquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TitleWhite

@Composable
fun MainMenuScreen(
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Menú Principal",
                color = TitleWhite,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Aquí irán las categorías del juego

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
            ) {
                Text("Cerrar Sesión", color = TitleWhite)
            }
        }
    }
}