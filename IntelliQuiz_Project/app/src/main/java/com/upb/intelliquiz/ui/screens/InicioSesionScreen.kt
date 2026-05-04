package com.upb.intelliquiz.ui.screens

import android.media.MediaPlayer
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
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.*
import com.upb.intelliquiz.utils.AuthViewModel
import com.upb.intelliquiz.utils.AuthState
import java.util.regex.Pattern

@Composable
fun InicioSesionScreen(
    onBackPressed: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegistrate: () -> Unit,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var emailRecuperacion by remember { mutableStateOf("") }

    // Estados de error para validación local
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Observar estados del ViewModel
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()

    // Validar email
    fun validarEmail(email: String): Boolean {
        val regex = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)\$")
        return regex.matcher(email).matches()
    }

    // Validar contraseña no vacía
    fun validarPassword(password: String): Boolean {
        return password.isNotEmpty()
    }

    // Función para intentar login
    fun intentarLogin() {
        emailError = null
        passwordError = null

        // Validar email
        if (email.isEmpty()) {
            emailError = "Ingresa tu correo electrónico"
            return
        }
        if (!validarEmail(email)) {
            emailError = "Ingresa un correo válido (ejemplo: usuario@mail.com)"
            return
        }

        // Validar contraseña
        if (password.isEmpty()) {
            passwordError = "Ingresa tu contraseña"
            return
        }

        // Si todo está bien, intentar login
        mediaPlayer?.start()
        authViewModel.loginWithEmail(email, password)
    }

    // Cargar sonido
    LaunchedEffect(Unit) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.button_click)
            mediaPlayer?.setVolume(0.7f, 0.7f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Liberar sonido
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    // Manejar autenticación exitosa
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
            onLoginSuccess()
        }
    }

    // Mostrar errores de Firebase
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            authViewModel.clearError()
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
            Spacer(modifier = Modifier.height(75.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ButtonCircleDark)
                        .clickable {
                            mediaPlayer?.start()
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
                Spacer(modifier = Modifier.width(40.dp))
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Hola! Bienvenido de Vuelta",
                color = TitleWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Inicia sesión para retar tu mente hoy. Cada pregunta es una oportunidad para brillar.",
                color = TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Email",
                    color = TitleWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 4.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TitleWhite)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Campo Email
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    placeholder = { Text("Correo Electrónico", color = TextGray.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = emailError != null,
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
                        Icon(painter = painterResource(R.drawable.icon_mail), contentDescription = "Email", tint = TextGray, modifier = Modifier.size(20.dp))
                    }
                )
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    placeholder = { Text("Contraseña", color = TextGray.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = passwordError != null,
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
                        Icon(painter = painterResource(R.drawable.icon_password), contentDescription = "Contraseña", tint = TextGray, modifier = Modifier.size(20.dp))
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
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Olvidaste Tu Contraseña?",
                color = ButtonPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        emailRecuperacion = ""
                        showBottomSheet = true
                    },
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Iniciar Sesión
            Button(
                onClick = { intentarLogin() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = TitleWhite)
                } else {
                    Text("Iniciar Sesión", color = TitleWhite, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("No tienes una cuenta? ", color = TextGray, fontSize = 14.sp)
                Text(
                    "Registrate!",
                    color = ButtonPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        mediaPlayer?.start()
                        onRegistrate()
                    }
                )
            }
        }
    }

    // Diálogo para recuperar contraseña
    if (showBottomSheet) {
        Dialog(
            onDismissRequest = { showBottomSheet = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable { showBottomSheet = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundDark)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.width(40.dp).height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(TextGray.copy(alpha = 0.5f))
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Recuperar Contraseña", color = TitleWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = emailRecuperacion,
                            onValueChange = { emailRecuperacion = it },
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
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (emailRecuperacion.isNotEmpty()) {
                                    authViewModel.sendPasswordResetEmail(emailRecuperacion)
                                    showBottomSheet = false
                                } else {
                                    Toast.makeText(context, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
                        ) {
                            Text("Enviar Link", color = TitleWhite, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
