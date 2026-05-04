package com.upb.intelliquiz.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Estados de autenticación
sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    object Loading : AuthState()
}

class AuthViewModel(private val context: Context) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _authState.value = AuthState.Authenticated(currentUser)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    // Registro con email y contraseña
    fun registerWithEmail(email: String, password: String, nombreCompleto: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user

                if (user != null) {
                    val userData = hashMapOf(
                        "uid" to user.uid,
                        "nombreCompleto" to nombreCompleto,
                        "email" to email,
                        "fechaRegistro" to System.currentTimeMillis(),
                        "puntuacionTotal" to 0,
                        "partidasJugadas" to 0,
                        "respuestasCorrectas" to 0
                    )

                    firestore.collection("usuarios").document(user.uid)
                        .set(userData)
                        .await()

                    _authState.value = AuthState.Authenticated(user)
                }
            } catch (e: Exception) {
                _errorMessage.value = when {
                    e.message?.contains("email already in use") == true -> "Este correo ya está registrado"
                    e.message?.contains("password is weak") == true -> "Contraseña muy débil"
                    else -> e.message ?: "Error en el registro"
                }
                Log.e("AuthViewModel", "Error en registro: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Inicio de sesión con email y contraseña
    fun loginWithEmail(email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user

                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                }
            } catch (e: Exception) {
                _errorMessage.value = when {
                    e.message?.contains("invalid email") == true -> "Correo inválido"
                    e.message?.contains("wrong password") == true -> "Contraseña incorrecta"
                    e.message?.contains("user not found") == true -> "Usuario no encontrado"
                    else -> e.message ?: "Error en el inicio de sesión"
                }
                Log.e("AuthViewModel", "Error en login: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Enviar correo de recuperación
    fun sendPasswordResetEmail(email: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                _errorMessage.value = "Correo de recuperación enviado"
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error al enviar el correo"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cerrar sesión
    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    // Obtener usuario actual
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Limpiar errores
    fun clearError() {
        _errorMessage.value = null
    }
}

// Factory para crear ViewModel con contexto
class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}