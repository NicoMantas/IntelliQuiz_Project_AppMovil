package com.upb.intelliquiz.ui.screens

import android.media.MediaPlayer
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.components.IntelliQuizBottomNavBar
import com.upb.intelliquiz.ui.components.NavSection
import com.upb.intelliquiz.ui.theme.BackgroundDark
import com.upb.intelliquiz.ui.theme.ButtonCircleDark
import com.upb.intelliquiz.ui.theme.ButtonPurple
import com.upb.intelliquiz.ui.theme.TextGray
import com.upb.intelliquiz.ui.theme.TitleWhite
import com.upb.intelliquiz.utils.AuthViewModel

/**
 * Pantalla de perfil del usuario. Reutiliza el AuthViewModel existente para
 * cargar/actualizar datos en Firestore, recuperar contraseña y cerrar sesión.
 *
 * Los inputs respetan exactamente el estilo de RegistroScreen / InicioSesionScreen.
 */
@Composable
fun PerfilScreen(
    onHomeClick: () -> Unit,
    onJugarClick: () -> Unit,
    onPuntajeClick: () -> Unit,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()

    var nombre by remember { mutableStateOf("") }
    var nombreInicial by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nombreError by remember { mutableStateOf<String?>(null) }
    var savingProfile by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
            mediaPlayer?.setVolume(0.7f, 0.7f)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Cargar datos iniciales del usuario
        authViewModel.getCurrentUserData { data ->
            val nombreCompleto = (data?.get("nombreCompleto") as? String) ?: ""
            val correo = (data?.get("email") as? String)
                ?: authViewModel.getCurrentUser()?.email
                ?: ""
            nombre = nombreCompleto
            nombreInicial = nombreCompleto
            email = correo
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                authViewModel.getCurrentUserData { data ->
                    val nombreCompleto = (data?.get("nombreCompleto") as? String) ?: nombre
                    val correo = (data?.get("email") as? String)
                        ?: authViewModel.getCurrentUser()?.email
                        ?: email
                    if (nombre.isBlank() || nombre == nombreInicial) {
                        nombre = nombreCompleto
                        nombreInicial = nombreCompleto
                    }
                    if (email.isBlank()) email = correo
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    fun playClick() {
        try {
            mediaPlayer?.let { mp -> if (!mp.isPlaying) mp.start() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun validarNombre(value: String): Boolean {
        val regex = Regex("^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]+$")
        return regex.matches(value)
    }

    val nombreCambiado = nombre.trim().isNotEmpty() && nombre.trim() != nombreInicial.trim()

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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Mi Perfil",
                        color = BackgroundDark,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(TextGray.copy(alpha = 0.25f))
                            .border(2.dp, ButtonPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PersonOutline,
                            contentDescription = "Avatar",
                            tint = BackgroundDark,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Cambiar Foto",
                        color = ButtonPurple,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            playClick()
                            Toast.makeText(
                                context,
                                "Función disponible próximamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Nombre Completo (mismo estilo que RegistroScreen)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = {
                                nombre = it
                                nombreError = null
                            },
                            placeholder = {
                                Text(
                                    "Nombre Completo",
                                    color = TextGray.copy(alpha = 0.5f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = nombreError != null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ButtonPurple,
                                unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                                focusedTextColor = BackgroundDark,
                                unfocusedTextColor = BackgroundDark,
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
                        if (nombreError != null) {
                            Text(
                                text = nombreError!!,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Correo Electrónico (sólo lectura)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { },
                        placeholder = {
                            Text(
                                "Correo Electrónico",
                                color = TextGray.copy(alpha = 0.5f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ButtonPurple,
                            unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                            focusedTextColor = BackgroundDark,
                            unfocusedTextColor = BackgroundDark,
                            disabledBorderColor = TextGray.copy(alpha = 0.3f),
                            disabledTextColor = BackgroundDark,
                            disabledLeadingIconColor = TextGray,
                            disabledPlaceholderColor = TextGray.copy(alpha = 0.5f),
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

                    Spacer(modifier = Modifier.height(28.dp))

                    // Guardar cambios (sólo visible cuando hay cambios)
                    if (nombreCambiado) {
                        Button(
                            onClick = {
                                if (!validarNombre(nombre.trim())) {
                                    nombreError = "El nombre solo debe contener letras"
                                    return@Button
                                }
                                playClick()
                                savingProfile = true
                                authViewModel.updateUserName(nombre.trim()) { ok ->
                                    savingProfile = false
                                    if (ok) {
                                        nombreInicial = nombre.trim()
                                        Toast.makeText(
                                            context,
                                            "Perfil actualizado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No se pudo actualizar el perfil",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple),
                            enabled = !savingProfile
                        ) {
                            Text(
                                text = if (savingProfile) "Guardando…" else "Guardar Cambios",
                                color = TitleWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Recuperar Contraseña
                    Button(
                        onClick = {
                            if (email.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "No se pudo obtener tu correo",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            playClick()
                            authViewModel.sendPasswordResetEmail(email)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(2.dp, ButtonPurple, RoundedCornerShape(28.dp)),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TitleWhite),
                        enabled = !isLoading
                    ) {
                        Text(
                            text = "Recuperar Contraseña",
                            color = ButtonPurple,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cerrar Sesión
                    Button(
                        onClick = {
                            playClick()
                            onLogout()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonCircleDark)
                    ) {
                        Text(
                            text = "Cerrar Sesión",
                            color = TitleWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tu progreso se guarda automáticamente",
                        color = TextGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        IntelliQuizBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selected = NavSection.Perfil,
            onHomeClick = onHomeClick,
            onJugarClick = onJugarClick,
            onPuntajeClick = onPuntajeClick
        )
    }
}
