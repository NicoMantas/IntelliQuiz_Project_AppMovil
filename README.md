# IntelliQuiz - App de Preguntas y Respuestas

## Descripción corta

IntelliQuiz es una aplicación móvil interactiva que desafía tus conocimientos en múltiples categorías como Historia, Ciencia, Matemáticas e Inglés. Compite con amigos, compara puntuaciones y demuestra quién es el verdadero sabio.

## Descripción larga

¿Eres un apasionado del conocimiento? ¿Te gusta demostrar lo que sabes y aprender cosas nuevas? IntelliQuiz es la aplicación perfecta para ti. Con una interfaz moderna y atractiva, podrás poner a prueba tu mente en diferentes áreas del saber.

La aplicación te permite elegir entre diversas categorías de preguntas, desde desafíos académicos hasta temas de cultura general. Cada pregunta está diseñada para retarte y ayudarte a mejorar tus conocimientos. Además, puedes competir con amigos y otros jugadores, comparando puntuaciones y subiendo en el ranking global.

IntelliQuiz no solo es un juego, es una herramienta de aprendizaje que te motiva a seguir descubriendo y adquiriendo nuevos conocimientos mientras te diviertes. Con un sistema de puntuación dinámico y una interfaz intuitiva, la experiencia de aprendizaje se convierte en un reto emocionante.

## Tecnologías

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose (UI declarativa moderna)
- **Navegación:** Navigation Compose
- **Backend:** Firebase Authentication, Firebase Firestore
- **Autenticación:** Email/Password, Google Sign In, Apple Sign In
- **Base de datos en tiempo real:** Firestore para almacenar usuarios y puntuaciones
- **IDE:** Android Studio

### 📂 Estructura del Proyecto

```
app/
├── src/main/java/com/upb/intelliquiz/
│   ├── MainActivity.kt
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── SplashScreen.kt
│   │   │   ├── OnboardingScreen.kt
│   │   │   ├── InicioScreen.kt
│   │   │   ├── InicioSesionScreen.kt
│   │   │   ├── RegistroScreen.kt
│   │   │   └── MainMenuScreen.kt
│   │   └── theme/
│   │       ├── Color.kt
│   │       ├── Theme.kt
│   │       └── Type.kt
│   └── utils/
│       └── AuthViewModel.kt
├── res/
│   ├── drawable/
│   ├── raw/ (sonidos)
│   └── values/
└── google-services.json
```

## Funcionalidades implementadas

### Pantallas
- **SplashScreen:** Animación de entrada con logo y nombre de la app (2.5 segundos)
- **OnboardingScreen:** Dos pantallas introductorias con animaciones y líneas indicadoras
- **InicioScreen:** Pantalla de bienvenida con logo, mensajes y opciones de acceso
- **InicioSesionScreen:** Login con email/contraseña, validaciones y diálogo para recuperar contraseña
- **RegistroScreen:** Registro de usuarios con validaciones en tiempo real
- **MainMenuScreen:** Pantalla principal del juego (en desarrollo)

### Autenticación y Base de Datos
- Registro de usuarios con Firebase Authentication
- Inicio de sesión con email y contraseña
- Recuperación de contraseña mediante enlace de correo electrónico
- Almacenamiento de datos de usuario en Firestore (nombre, email, puntuaciones, etc.)
- Validación de campos en tiempo real (email, nombre, contraseña)

### Experiencia de usuario
- **Sonidos interactivos:** Feedback auditivo en botones y acciones
- **Animaciones suaves:** Transiciones fluidas entre pantallas
- **Diálogos modales:** Para recuperación de contraseña sin cambiar de pantalla
- **Persistencia de sesión:** El usuario permanece logueado entre sesiones
- **Diseño responsivo:** Adaptable a diferentes tamaños de pantalla

## Diseño

El diseño fue creado en Figma, así como sus vistas con el prototipo y su posible navegación de la aplicación.

### Figma

https://www.figma.com/design/LR1VJ12PpXqmZC95Q04zXm/IntelliQuiz-Entrega-Final-APP-MOVILES?node-id=0-1&t=FCNIq7YcZ3Q0vL1a-1

### Paleta de colores dado del Figma

| Uso | Color | Código |
|-----|-------|--------|
| Fondo principal | Azul oscuro | `#1A1A2E` |
| Títulos | Blanco | `#FFFFFF` |
| Texto secundario | Gris claro | `#B8B8D0` |
| Botones primarios | Morado | `#6C63FF` |
| Botones secundarios | Azul marino | `#25326D` |

## Cómo ejecutar el proyecto



## Autores

1. **Nicolas Mantilla Gelves**
2. **Santiago Maya Horta**
3. **Carlos Danilo Velez Castro**
