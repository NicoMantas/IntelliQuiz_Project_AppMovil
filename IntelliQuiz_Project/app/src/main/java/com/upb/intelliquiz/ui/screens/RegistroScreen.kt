package com.upb.intelliquiz.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.*

@Composable
fun RegistroScreen(
    onBackPressed: () -> Unit,
    onRegistroSuccess: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    var nombreCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            // Fila con icono pequeño, título y flecha de volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flecha de volver (izquierda) - CON SONIDO
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ButtonCircleDark)
                        .clickable {
                            try {
                                mediaPlayer?.let { mp ->
                                    if (!mp.isPlaying) {
                                        mp.start()
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            onBackPressed()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = TitleWhite,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Icono y título centrados
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_page),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "IntelliQuiz",
                        color = TitleWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Espacio para mantener el centrado
                Spacer(modifier = Modifier.width(40.dp))
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Título
            Text(
                text = "Registrate en IntelliQuiz",
                color = TitleWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = "Regístrate y forma parte de la comunidad que compite cada día por llegar a la cima del conocimiento.",
                color = TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Campo Nombre Completo
            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                placeholder = {
                    Text(
                        text = "Nombre Completo",
                        color = TextGray.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ButtonPurple,
                    unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                    focusedTextColor = TitleWhite,
                    unfocusedTextColor = TitleWhite,
                    cursorColor = ButtonPurple
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_user),
                        contentDescription = "Nombre",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Correo Electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = "Correo Electrónico",
                        color = TextGray.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ButtonPurple,
                    unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                    focusedTextColor = TitleWhite,
                    unfocusedTextColor = TitleWhite,
                    cursorColor = ButtonPurple
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_mail),
                        contentDescription = "Email",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            text = "Contraseña",
                            color = TextGray.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ButtonPurple,
                        unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                        focusedTextColor = TitleWhite,
                        unfocusedTextColor = TitleWhite,
                        cursorColor = ButtonPurple
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_password),
                            contentDescription = "Contraseña",
                            tint = TextGray,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                                ),
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                tint = TextGray
                            )
                        }
                    }
                )

                // Texto "Min. 8 caracteres"
                Text(
                    text = "Min. 8 caracteres",
                    color = TextGray.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Botón Registrate - CON SONIDO
            Button(
                onClick = {
                    try {
                        mediaPlayer?.let { mp ->
                            if (!mp.isPlaying) {
                                mp.start()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    onRegistroSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
            ) {
                Text(
                    text = "Registrate",
                    color = TitleWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Texto "Tienes una cuenta? Inicia Sesion!" (SIN SONIDO - solo navegación)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tienes una cuenta? ",
                    color = TextGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Inicia Sesion!",
                    color = ButtonPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onIniciarSesion()
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    IntelliQuizTheme {
        RegistroScreen(
            onBackPressed = {},
            onRegistroSuccess = {},
            onIniciarSesion = {}
        )
    }
}