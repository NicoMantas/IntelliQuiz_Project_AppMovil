package com.upb.intelliquiz.utils

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.Query
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp

// Estados de autenticación
sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    object Loading : AuthState()
}

class AuthViewModel(private val context: Context) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Usar StateFlow en lugar de LiveData
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _authState.value = AuthState.Authenticated(currentUser)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    // Obtener datos del usuario actual desde Firestore
    fun getCurrentUserData(onResult: (HashMap<String, Any>?) -> Unit) {
        val currentUser = auth.currentUser ?: return onResult(null)

        viewModelScope.launch {
            try {
                val document = firestore.collection("usuarios").document(currentUser.uid).get().await()
                val data = document.data as? HashMap<String, Any>
                onResult(data)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error al obtener datos: ${e.message}")
                onResult(null)
            }
        }
    }

    // Actualizar datos del usuario
    fun updateUserData(updates: Map<String, Any>) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                firestore.collection("usuarios").document(currentUser.uid)
                    .update(updates)
                    .await()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error al actualizar: ${e.message}")
            }
        }
    }

    // Obtener ranking de usuarios (top 10 por puntuación)
    fun getRanking(onResult: (List<HashMap<String, Any>>?) -> Unit) {
        viewModelScope.launch {
            try {
                val query = firestore.collection("usuarios")
                    .orderBy("puntuacionTotal", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()

                val ranking = query.documents.mapNotNull { it.data as? HashMap<String, Any> }
                onResult(ranking)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error al obtener ranking: ${e.message}")
                onResult(null)
            }
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
                        "fechaRegistro" to Timestamp.now(),
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
                ToastManager.show(context, "Correo de recuperación enviado")
                _errorMessage.value = null
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

// Helper para Toast (opcional)
object ToastManager {
    fun show(context: Context, message: String) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}