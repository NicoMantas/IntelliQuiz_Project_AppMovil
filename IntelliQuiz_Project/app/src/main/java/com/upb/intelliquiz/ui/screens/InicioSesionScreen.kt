package com.upb.intelliquiz.ui.screens

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
import com.upb.intelliquiz.R
import com.upb.intelliquiz.ui.theme.*

@Composable
fun InicioSesionScreen(
    onBackPressed: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegistrate: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para el BottomSheet
    var showBottomSheet by remember { mutableStateOf(false) }
    var step by remember { mutableStateOf(1) }
    var emailRecuperacion by remember { mutableStateOf("") }

    val context = LocalContext.current

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

            // Fila con icono pequeño, título y flecha de volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flecha de volver (izquierda)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ButtonCircleDark)
                        .clickable { onBackPressed() },
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

            // Línea "Email"
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text("Correo Electrónico", color = TextGray.copy(alpha = 0.5f))
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
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text("Contraseña", color = TextGray.copy(alpha = 0.5f))
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
                                if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                            tint = TextGray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Olvidaste Tu Contraseña? - Abre el BottomSheet
            Text(
                text = "Olvidaste Tu Contraseña?",
                color = ButtonPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        step = 1
                        emailRecuperacion = ""
                        showBottomSheet = true
                    },
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Iniciar Sesión
            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
            ) {
                Text("Iniciar Sesión", color = TitleWhite, fontSize = 18.sp, fontWeight = FontWeight.Medium)
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
                    modifier = Modifier.clickable { onRegistrate() }
                )
            }
        }
    }

    // BottomSheet de 2 pasos
    if (showBottomSheet) {
        Dialog(
            onDismissRequest = {
                showBottomSheet = false
                step = 1
                emailRecuperacion = ""
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable {
                        showBottomSheet = false
                        step = 1
                        emailRecuperacion = ""
                    }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Línea indicadora
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(TextGray.copy(alpha = 0.5f))
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // PASO 1: Elegir método
                        if (step == 1) {
                            // Icono
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(ButtonPurple.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_mail),
                                    contentDescription = "Recuperar",
                                    tint = ButtonPurple,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Olvidaste Tu Contraseña?",
                                color = TitleWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Por favor elige el método para recuperar tu contraseña.",
                                color = TextGray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Opción: Correo Electrónico
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { step = 2 },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = ButtonCircleDark.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(ButtonPurple),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_mail),
                                            contentDescription = "Correo",
                                            tint = TitleWhite,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Correo Electrónico", color = TitleWhite, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                        Text("Recibirás un link en tu correo", color = TextGray, fontSize = 12.sp)
                                    }

                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                                        contentDescription = "Seleccionar",
                                        tint = ButtonPurple,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            TextButton(onClick = { showBottomSheet = false }) {
                                Text("Cancelar", color = TextGray, fontSize = 14.sp)
                            }
                        }

                        // PASO 2: Ingresar correo
                        if (step == 2) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(ButtonPurple.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_mail),
                                    contentDescription = "Email",
                                    tint = ButtonPurple,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Recuperar Contraseña", color = TitleWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Ingresa tu correo y te enviaremos un link para crear una nueva contraseña",
                                color = TextGray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(32.dp))

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
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_mail),
                                        contentDescription = null,
                                        tint = TextGray,
                                        modifier = Modifier.size(20.dp)
                                    )                                }
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = {
                                    if (emailRecuperacion.isNotEmpty()) {
                                        Toast.makeText(context, "Link enviado a $emailRecuperacion", Toast.LENGTH_SHORT).show()
                                        showBottomSheet = false
                                        step = 1
                                        emailRecuperacion = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
                            ) {
                                Text("Enviar Link", color = TitleWhite, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            TextButton(onClick = { step = 1 }) {
                                Text("Volver", color = TextGray, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioSesionScreenPreview() {
    IntelliQuizTheme {
        InicioSesionScreen(
            onBackPressed = {},
            onLoginSuccess = {},
            onRegistrate = {}
        )
    }
}