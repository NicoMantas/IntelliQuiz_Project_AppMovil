package com.upb.intelliquiz.ui.screens

import android.media.MediaPlayer
import com.upb.intelliquiz.utils.AuthState
import android.widget.Toast
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.*
import com.upb.intelliquiz.utils.AuthViewModel


@Composable
fun RegistroScreen(
    onBackPressed: () -> Unit,
    onRegistroSuccess: () -> Unit,
    onIniciarSesion: () -> Unit,
    authViewModel: AuthViewModel
) {
    var nombreCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Observar estados del ViewModel - CORREGIDO: usar collectAsStateWithLifecycle
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()

    // Cargar sonido
    LaunchedEffect(Unit) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
            mediaPlayer?.setVolume(0.7f, 0.7f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    // Manejar registro exitoso
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            Toast.makeText(context, "Registro exitoso!", Toast.LENGTH_SHORT).show()
            onRegistroSuccess()
        }
    }

    // Mostrar errores
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            authViewModel.clearError()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(ButtonCircleDark)
                        .clickable {
                            mediaPlayer?.start()
                            onBackPressed()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = TitleWhite,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_page),
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
                Spacer(modifier = Modifier.width(40.dp))
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Registrate en IntelliQuiz",
                color = TitleWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Regístrate y forma parte de la comunidad que compite cada día por llegar a la cima del conocimiento.",
                color = TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))

            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                placeholder = { Text("Nombre Completo", color = TextGray.copy(alpha = 0.5f)) },
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
                        painter = painterResource(R.drawable.icon_user),
                        contentDescription = "Nombre",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo Electrónico", color = TextGray.copy(alpha = 0.5f)) },
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
                        painter = painterResource(R.drawable.icon_mail),
                        contentDescription = "Email",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña", color = TextGray.copy(alpha = 0.5f)) },
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
                        painter = painterResource(R.drawable.icon_password),
                        contentDescription = "Contraseña",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                            tint = TextGray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                    mediaPlayer?.start()
                    when {
                        nombreCompleto.isEmpty() -> Toast.makeText(context, "Ingresa tu nombre completo", Toast.LENGTH_SHORT).show()
                        email.isEmpty() -> Toast.makeText(context, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
                        password.length < 6 -> Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                        else -> authViewModel.registerWithEmail(email, password, nombreCompleto)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = TitleWhite)
                } else {
                    Text("Registrate", color = TitleWhite, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Tienes una cuenta? ", color = TextGray, fontSize = 14.sp)
                Text(
                    text = "Inicia Sesion!",
                    color = ButtonPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        mediaPlayer?.start()
                        onIniciarSesion()
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}