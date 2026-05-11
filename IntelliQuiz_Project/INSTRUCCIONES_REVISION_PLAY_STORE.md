# INSTRUCCIONES PARA REVISIÓN PLAY STORE

**Aplicación:** IntelliQuiz
**Paquete (Application ID):** `com.upb.intelliquiz`
**Versión:** 1.0 (versionCode 1)
**SDK mínimo:** Android 7.0 (API 24)
**SDK objetivo / compilación:** Android 16 (API 36)
**Idioma de la aplicación:** Español
**Categoría sugerida en Play:** Educación / Trivia

---

## 1. DESCRIPCIÓN DE LA APLICACIÓN

IntelliQuiz es una aplicación móvil de preguntas y respuestas (trivia educativa) desarrollada en **Kotlin** con **Jetpack Compose**. Permite al usuario poner a prueba sus conocimientos en cinco categorías: **Ciencia, Matemáticas, Inglés, Sociales y Aleatorio**. Cada partida presenta preguntas de opción múltiple con un temporizador por pregunta, sistema de vidas, racha de aciertos y puntuación acumulada.

La aplicación cuenta con un sistema de cuentas de usuario respaldado por **Firebase Authentication** (correo y contraseña) y **Cloud Firestore** para almacenar el perfil del usuario, su puntuación total, partidas jugadas, respuestas correctas, trofeos, puntuación por categoría y un ranking entre usuarios.

---

## 2. OBJETIVO PRINCIPAL DE LA APP

Brindar al usuario una experiencia educativa y de entretenimiento donde pueda:

- Resolver preguntas de opción múltiple por categoría académica.
- Acumular puntos, trofeos y rachas.
- Visualizar un ranking global (Top 10) y un ranking de amigos.
- Consultar su puntuación desglosada por categoría.
- Gestionar su perfil (nombre completo, correo, recuperación de contraseña, cierre de sesión).

---

## 3. TECNOLOGÍAS UTILIZADAS

| Componente | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose 2.9.8 |
| Autenticación | Firebase Authentication (Email/Password) |
| Base de datos | Cloud Firestore |
| Métricas | Firebase Analytics |
| Almacenamiento local | DataStore Preferences |
| Audio | `android.media.MediaPlayer` (sonido de clics) |
| Concurrencia | Kotlin Coroutines + Flow |
| Mínimo Android | 7.0 (API 24) |
| Objetivo Android | 16 (API 36) |

**Proyecto Firebase asociado:** `intelliquiz-appmobile-entrega` (configurado vía `google-services.json`).

---

## 4. PERMISOS DECLARADOS

La aplicación declara únicamente el siguiente permiso en `AndroidManifest.xml`:

| Permiso | Justificación |
|---|---|
| `android.permission.MODIFY_AUDIO_SETTINGS` | Necesario para ajustar el volumen del sonido de clic en botones reproducido mediante `MediaPlayer` al interactuar con la interfaz. |

La aplicación **no solicita permisos sensibles** (ubicación, cámara, micrófono, contactos, almacenamiento, SMS, llamadas, Bluetooth, etc.).

---

## 5. CREDENCIALES DE PRUEBA PARA EL REVISOR

> **IMPORTANTE:** Esta cuenta está creada en el backend (Firebase Authentication + Firestore) para que el equipo de revisión pueda acceder de forma inmediata sin tener que registrarse.

```
Correo:      prueba@mail.com
Contraseña:  12345678
```

Con esta cuenta el revisor accede a la totalidad de las funcionalidades de la aplicación. No existen funcionalidades premium, de pago, ni regiones bloqueadas.

> Si el revisor lo prefiere, también puede crear su propia cuenta desde la pantalla **Regístrate** usando cualquier correo electrónico válido y una contraseña de mínimo 8 caracteres.

---

## 6. REQUISITOS DE CONEXIÓN

- **La aplicación requiere conexión a internet** para:
  - Iniciar sesión / registrarse (Firebase Authentication).
  - Guardar y leer datos del perfil y resultados de partida (Cloud Firestore).
  - Cargar el ranking global y el ranking de amigos.
  - Recuperar la contraseña por correo (envío de email vía Firebase).
- Las preguntas del quiz están **incluidas dentro del APK/AAB** (no se descargan de servidor), por lo que la lógica de juego puede ejecutarse aún con conectividad limitada una vez iniciada la sesión, aunque el guardado del resultado requerirá conexión.

---

## 7. FLUJO DE NAVEGACIÓN COMPLETO

El flujo se administra desde `MainActivity.kt` con `NavHost` (rutas declaradas en Navigation Compose).

```
[Splash] ──(2.5 s)──▶ [Onboarding (2 páginas)] ──▶ [Inicio (Bienvenida)]
                                                          │
                                ┌─────────────────────────┴─────────────────────────┐
                                ▼                                                   ▼
                      [Iniciar Sesión]                                          [Registro]
                                │                                                   │
                                └──────────────────────┬────────────────────────────┘
                                                       ▼
                                                 [Main Menu]
                                                       │
            ┌─────────────────┬─────────────────┬──────┴───────────────┐
            ▼                 ▼                 ▼                      ▼
      [Categorías]        [Puntaje]          [Perfil]               (Logout)
            │                                                       
            ▼                                                       
        [Juego]                                                     
            │                                                       
            ▼                                                       
       [Game Over]                                                  
```

**Comportamiento al abrir la app:**

- Si el usuario **ya inició sesión previamente** (sesión persistida por Firebase), la app arranca directamente en **Main Menu**.
- Si **no hay sesión activa**, la app arranca en **Splash → Onboarding → Inicio**.

---

## 8. PASOS EXACTOS PARA PROBAR TODAS LAS FUNCIONALIDADES

### 8.1. Primer arranque (sin sesión)

1. Abrir la aplicación.
2. Se mostrará la pantalla **Splash** con el logo durante ~2.5 segundos (animación automática).
3. Avanzará automáticamente al **Onboarding** (2 páginas informativas).
4. Pulsar **"Continuar"** dos veces para pasar las dos páginas del onboarding.
5. Se mostrará la pantalla **Inicio (Bienvenida)** con dos opciones: *Iniciar Sesión* y *Regístrate*.

### 8.2. Inicio de sesión con cuenta de prueba

1. En la pantalla **Inicio**, pulsar **"Iniciar Sesión"**.
2. Ingresar:
   - **Email:** `prueba@mail.com`
   - **Contraseña:** `12345678`
3. Pulsar **"Iniciar Sesión"**.
4. Aparecerá un toast **"¡Bienvenido!"** y la app navegará al **Main Menu**.

### 8.3. Registro de un nuevo usuario (opcional)

1. En la pantalla **Inicio** o en **Iniciar Sesión**, pulsar **"Regístrate"**.
2. Completar los tres campos:
   - **Nombre completo** (solo letras y espacios).
   - **Correo electrónico** con formato válido.
   - **Contraseña** de mínimo **8 caracteres**.
3. Pulsar **"Regístrate"**.
4. Se mostrará el toast **"Registro exitoso!"** y la app redirigirá al login para iniciar sesión con la nueva cuenta.

### 8.4. Recuperación de contraseña

1. En la pantalla **Iniciar Sesión**, pulsar **"Olvidaste Tu Contraseña?"**.
2. Se abrirá un diálogo inferior.
3. Ingresar un correo válido y pulsar **"Enviar Link"**.
4. Firebase enviará un correo de recuperación a esa dirección y se mostrará un toast de confirmación.

### 8.5. Main Menu

Tras el login, se muestra:

- Nombre del usuario, conteo de trofeos.
- Botón grande **"Partida Rápida"** que abre el listado de categorías.
- **Ranking Semanal** (Top 10 global cargado desde Firestore + posición del usuario actual al final si no está en el top).
- Barra inferior de navegación con cuatro secciones: **Home / Jugar / Puntaje / Perfil**.

### 8.6. Jugar una partida

1. Desde **Main Menu**, pulsar **"Partida Rápida"** (o en la barra inferior, **"Jugar"**).
2. Se abre **Categorías**. Seleccionar cualquiera de las cinco:
   - Ciencia 🧪
   - Matemáticas ➗
   - Inglés 🇬🇧
   - Sociales 📜
   - Aleatorio 🎲
3. Se abre la pantalla **Juego**:
   - Cabecera con número de pregunta (`x/3`), puntos, vidas (3 corazones) y cronómetro (10 s por pregunta).
   - Barra de progreso.
   - Pregunta y 4 opciones.
   - Botón **"Siguiente"** (o **"Finalizar"** en la última pregunta).
4. Seleccionar una respuesta y pulsar **"Siguiente"**.
   - Aparece un diálogo de feedback (✅ Correcto / ❌ Incorrecto) con racha y puntos ganados.
   - Si se acaba el tiempo, se marca como incorrecta y se pierde una vida.
5. Tras completar las preguntas (o agotar vidas), se navega a **Game Over** que muestra:
   - Categoría jugada.
   - Puntaje final, aciertos, incorrectas, tiempo total, racha máxima y porcentaje de precisión.
   - Botones **"Intentar Otra Vez"** (rejugar misma categoría) y **"Volver Home"**.
6. El resultado se persiste automáticamente en Firestore (puntuación, trofeos, partidas jugadas, respuestas correctas y puntuación por categoría).

### 8.7. Pantalla de Puntaje

1. En la barra inferior, pulsar **"Puntaje"**.
2. La pantalla tiene dos pestañas:
   - **Mi Puntaje:** muestra la puntuación del usuario desglosada por categoría (ordenada de mayor a menor).
   - **Mis Amigos:** muestra un podio con el top 3 y un listado tipo ranking. Si el usuario aún no ha agregado amigos, la app muestra el **Top 10 global** como respaldo para que la sección no quede vacía.

### 8.8. Pantalla de Perfil

1. En la barra inferior, pulsar **"Perfil"**.
2. Se muestra:
   - Avatar (icono genérico) y enlace **"Cambiar Foto"** (al pulsarlo aparece un toast informativo: "Función disponible próximamente").
   - Campo **Nombre Completo** editable: si se modifica aparece el botón **"Guardar Cambios"** que actualiza el nombre en Firestore.
   - Campo **Correo electrónico** en solo lectura.
   - Botón **"Recuperar Contraseña"**: envía un correo de restablecimiento al email de la cuenta.
   - Botón **"Cerrar Sesión"**: cierra sesión y regresa al flujo Splash → Onboarding → Inicio.

### 8.9. Cierre de sesión

- Desde **Main Menu** o desde **Perfil** se puede cerrar sesión.
- Al hacerlo, la sesión de Firebase Auth se invalida localmente y la app vuelve al flujo de bienvenida.

---

## 9. AUTENTICACIÓN Y MÓDULOS PROTEGIDOS

- **Todas las pantallas principales (Main Menu, Categorías, Juego, Game Over, Puntaje, Perfil) requieren sesión activa.**
- La sesión se administra mediante `FirebaseAuth` y se observa con un `StateFlow<AuthState>` en el ViewModel `AuthViewModel`.
- Estados posibles: `Unauthenticated`, `Authenticated(user)`, `Loading`.
- El revisor accede a todo el contenido protegido usando las **credenciales de prueba** entregadas en la sección 5.
- **No existen roles diferenciados** (admin/usuario/etc.). Todos los usuarios autenticados tienen acceso a las mismas funcionalidades.

---

## 10. INTEGRACIONES EXTERNAS

| Servicio | Uso |
|---|---|
| **Firebase Authentication** | Registro, login, recuperación de contraseña y persistencia de sesión por email/contraseña. |
| **Cloud Firestore** | Almacena la colección `usuarios` con: `uid`, `nombreCompleto`, `email`, `fechaRegistro`, `puntuacionTotal`, `trofeos`, `partidasJugadas`, `respuestasCorrectas`, `puntuacionPorCategoria` y `amigos`. |
| **Firebase Analytics** | Métricas anónimas de uso. |

La app **no integra publicidad, ni pasarelas de pago, ni compras dentro de la aplicación, ni notificaciones push**.

---

## 11. DATOS QUE RECOPILA LA APLICACIÓN

Para completar correctamente el **formulario de Seguridad de los Datos (Data Safety)** en Play Console, estos son los datos que efectivamente maneja la app:

| Tipo de dato | Recopilado | Compartido con terceros | Propósito | Opcional |
|---|---|---|---|---|
| **Correo electrónico** | Sí | Solo procesado por Firebase (Google) | Autenticación de cuenta | No |
| **Nombre del usuario** | Sí | Solo procesado por Firebase (Google) | Personalización del perfil y ranking | No |
| **Actividad en la app** (puntuación, partidas jugadas, respuestas correctas, trofeos, puntuación por categoría) | Sí | Solo procesado por Firebase (Google) | Funcionalidad central (puntuación y ranking) | No |
| **Identificadores de instalación / dispositivo (Firebase)** | Sí | Solo procesado por Firebase (Google) | Análisis y diagnóstico | No |

Todos los datos se transmiten **cifrados en tránsito** (HTTPS/TLS) por la infraestructura de Firebase. El usuario puede solicitar la eliminación de su cuenta contactando al equipo desarrollador (los datos se almacenan en la colección `usuarios/{uid}` de Firestore y pueden eliminarse manualmente).

---

## 12. VALIDACIONES IMPLEMENTADAS EN FORMULARIOS

| Campo | Regla |
|---|---|
| **Nombre completo (registro / perfil)** | No vacío, solo letras (incluye tildes y ñ) y espacios. |
| **Correo electrónico** | No vacío y debe coincidir con expresión regular `^[A-Za-z0-9+_.-]+@(.+)$`. |
| **Contraseña (registro)** | Mínimo **8 caracteres**. |
| **Contraseña (login)** | No vacía. |

Los errores se muestran inline debajo del campo correspondiente y los errores devueltos por Firebase se muestran como **Toast**.

---

## 13. FUNCIONALIDADES VISIBLES QUE PUEDEN GENERAR DUDAS AL REVISOR

A continuación se explican elementos de la interfaz que, en una revisión rápida, podrían parecer ambiguos:

1. **Botones "Apple" y "Google" en la pantalla Inicio (Bienvenida).**
   Aparecen visualmente como íconos de inicio de sesión social, pero en la versión actual **no realizan ninguna acción** al pulsarlos (no hay flujo SSO implementado). El único método de autenticación funcional en esta versión es **Email/Contraseña**. Esto es intencional para esta entrega y no oculta funcionalidad al revisor.

2. **Enlace "Cambiar Foto" en la pantalla de Perfil.**
   Al pulsarlo se muestra el toast **"Función disponible próximamente"**. No se carga ni se sube ninguna imagen.

3. **Ranking de amigos vacío.**
   Si la cuenta no tiene amigos agregados (es el caso de las cuentas nuevas y de la cuenta de prueba si no se han añadido amigos), la pestaña **"Mis Amigos"** muestra como respaldo el **Top 10 global** de jugadores. Esto es intencional para que la pantalla no quede vacía.

4. **Banco de preguntas reducido.**
   Cada categoría incluye actualmente **3 preguntas** definidas en código (`getQuestionsByCategory` en `JuegoScreen.kt`). Una partida termina cuando se contestan las 3 preguntas o se agotan las 3 vidas. Esta es la cantidad real disponible en esta versión.

5. **Sin notificaciones, sin publicidad, sin compras.**
   La aplicación no envía notificaciones, no muestra anuncios y no realiza ningún cobro.

---

## 14. CUMPLIMIENTO DE POLÍTICAS DE GOOGLE PLAY

| Política | Estado de cumplimiento basado en la app actual |
|---|---|
| **Permisos mínimos necesarios** | Cumple. Único permiso declarado: `MODIFY_AUDIO_SETTINGS`. |
| **Acceso completo para el revisor** | Cumple. Se proporcionan credenciales de prueba con acceso a todas las funcionalidades. |
| **Sin contenido oculto o detrás de barreras no documentadas** | Cumple. Todo el contenido es accesible tras iniciar sesión. No hay códigos secretos, regiones bloqueadas ni paywalls. |
| **Compatibilidad Android** | Cumple. `minSdk 24`, `targetSdk 36`. |
| **API de SDK objetivo** | Cumple con el requisito vigente de Google Play (targetSdk ≥ 35 / Android 15) al estar en API 36 (Android 16). |
| **Recopilación de datos declarada** | Documentada en la sección 11 para el formulario de Data Safety. |
| **Tráfico cifrado** | Cumple. Todas las llamadas a Firebase se realizan vía HTTPS/TLS. |
| **Contenido apropiado** | Cumple. Trivia educativa, sin contenido sensible. |
| **Sin funcionalidades engañosas** | Las funcionalidades no operativas (botones Apple/Google de la pantalla Inicio, "Cambiar Foto" en Perfil) están explícitamente declaradas en este documento (sección 13). |

---

## 15. TEXTO SUGERIDO PARA "NOTAS PARA EL EQUIPO DE REVISIÓN" (Play Console)

> Copiar el bloque siguiente directamente en el campo *Notas para el equipo de revisión* de Google Play Console:

```
Hola equipo de revisión,

IntelliQuiz es una aplicación educativa de trivia (preguntas y respuestas) en español, organizada por categorías (Ciencia, Matemáticas, Inglés, Sociales y Aleatorio). La aplicación requiere iniciar sesión para acceder al contenido principal. La autenticación se realiza mediante Firebase Authentication con correo y contraseña.

Para revisar la totalidad de funcionalidades, pueden usar la siguiente cuenta de prueba ya creada en nuestro backend:

  Correo:      prueba@mail.com
  Contraseña:  12345678

Flujo recomendado para la revisión:
1. Abrir la app → pasar Splash y Onboarding.
2. En la pantalla "Inicio" pulsar "Iniciar Sesión".
3. Ingresar las credenciales anteriores y pulsar "Iniciar Sesión".
4. En el Menú Principal pulsar "Partida Rápida" → elegir cualquier categoría → contestar las preguntas → ver la pantalla Game Over.
5. Probar la barra inferior: Home, Jugar (categorías), Puntaje (Mi Puntaje / Mis Amigos), Perfil.
6. En Perfil pueden probar "Recuperar Contraseña" y "Cerrar Sesión".

Información adicional:
- La app requiere conexión a internet (Firebase Authentication + Firestore).
- Único permiso solicitado: MODIFY_AUDIO_SETTINGS (necesario para el feedback sonoro de los botones).
- No incluye publicidad, ni compras dentro de la app, ni notificaciones, ni contenido para adultos.
- No solicita permisos sensibles (ubicación, cámara, micrófono, contactos, almacenamiento, etc.).
- En la pantalla "Inicio" aparecen iconos visuales de Apple y Google; en esta versión únicamente está habilitado el inicio de sesión por correo electrónico y contraseña.
- En el perfil, el enlace "Cambiar Foto" muestra un mensaje "Función disponible próximamente"; no carga ni sube imágenes.

Cualquier duda quedamos atentos. Gracias por su revisión.
```

---

## 16. CHECKLIST FINAL ANTES DE PUBLICAR

Marcar cada punto al verificarlo antes de enviar a revisión:

- [ ] La cuenta de prueba `prueba@mail.com / 12345678` está creada en Firebase Authentication y tiene su documento correspondiente en la colección `usuarios` de Firestore.
- [ ] La cuenta de prueba puede iniciar sesión correctamente desde el APK/AAB de release.
- [ ] El AAB (App Bundle) está firmado y subido a Play Console.
- [ ] El formulario de **Seguridad de los Datos (Data Safety)** está completo con la información de la sección 11 de este documento.
- [ ] La **Política de Privacidad pública** está enlazada en la ficha de Play (requerida porque la app recopila correo, nombre y actividad en la app).
- [ ] El **cuestionario de Clasificación de Contenido (IARC)** está respondido (la app es apta para todo público, sin contenido sensible).
- [ ] La descripción de la app y los textos de la ficha coinciden con las funcionalidades reales descritas en este documento.
- [ ] Las capturas de pantalla subidas a Play corresponden a la versión actual.
- [ ] El icono de la app y el feature graphic están subidos.
- [ ] El correo de contacto del desarrollador está configurado en Play Console.
- [ ] El bloque de texto de la sección 15 está copiado en **Notas para el equipo de revisión**.
- [ ] Verificado que la app abre sin errores en al menos un dispositivo físico o emulador con Android 7.0 (API 24) y otro con una versión reciente (API 33+).
- [ ] Verificado que existe conexión a internet al ejecutar pruebas (la app requiere internet).

---

## 17. INFORMACIÓN ÚTIL ADICIONAL PARA EL REVISOR

- **Idioma de la interfaz:** Español (Latinoamérica).
- **Orientación:** Portrait (vertical).
- **Tamaño del banco de preguntas por categoría en esta versión:** 3 preguntas.
- **Tiempo por pregunta:** 10 segundos.
- **Vidas por partida:** 3.
- **Puntos por respuesta correcta:** 10.
- **Persistencia de sesión:** Sí (Firebase mantiene la sesión activa entre aperturas de la app hasta que el usuario pulse "Cerrar Sesión").
- **Backups automáticos del sistema Android:** habilitados (`allowBackup="true"` con reglas predeterminadas).
- **Contacto técnico responsable de la app:** equipo desarrollador IntelliQuiz (Nicolás Mantilla Gelves, Santiago Maya Horta, Carlos Danilo Vélez Castro).

---

*Este documento describe el estado actual de la aplicación tal como se encuentra en su código fuente y está destinado al equipo de revisión de Google Play Console para facilitar la evaluación completa de IntelliQuiz.*
