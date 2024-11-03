package com.example.gymapp.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gymapp.R
import kotlin.random.Random

fun saveLoginData(sharedPreferences: SharedPreferences, username: String) {
    val editor = sharedPreferences.edit()
    editor.putString("username", username)
    editor.apply()
}

@Composable
fun WelcomeScreen(
    username: String?,
    passwordSelected: String?,
    navController: NavController,
    userProfileImageUrl: String? = null
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
    saveLoginData(sharedPreferences, username!!)

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showActivitiesDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val isLoginMessageShown = sharedPreferences.getBoolean("login_message_shown", false)

    // Mostrar el mensaje de inicio de sesión solo si es la primera vez
    LaunchedEffect(Unit) {
        if (!isLoginMessageShown) {
            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().putBoolean("login_message_shown", true).apply()
        }
    }

    // Frases motivacionales y selección aleatoria
    val motivationalPhrases = listOf(
        "“El dolor que sientes hoy será la fuerza que sentirás mañana.”",
        "“Cada día es una nueva oportunidad para mejorar.”",
        "“La disciplina supera al talento.”",
        "“No pares hasta que estés orgulloso.”",
        "“No sueñes con los resultados, trabaja por ellos.”"
    )
    val randomPhrase = motivationalPhrases.random()

    // Progreso semanal aleatorio
    val randomProgress = remember { Random.nextFloat() }

    // Actividades recientes aleatorias
    val recentActivities = listOf(
        Activity("Completaste 3 series de Sentadillas"),
        Activity("Alcanzaste un nuevo récord en Press de Banca"),
        Activity("Completaste una carrera de 5 km"),
        Activity("Hiciste 20 minutos de abdominales"),
        Activity("Aumentaste el peso en press de banca")
    )
    val randomActivities = remember { recentActivities.shuffled().take(2) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo en la parte superior
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1000))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Logo de la App",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Perfil del usuario
                UserProfile(username = username, imageUrl = userProfileImageUrl)

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de navegación
                AnimatedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        navController.navigate("Trainings_Screen")
                    },
                    text = "Entrenamientos",
                    icon = Icons.Filled.FitnessCenter
                )

                AnimatedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        navController.navigate("Weight_Calculator_Screen")
                    },
                    text = "Calculadora de Pesos",
                    icon = Icons.Filled.History
                )

                // Frase motivacional aleatoria
                Text(
                    text = randomPhrase,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
                )

                // Tarjeta de progreso semanal con valor aleatorio
                WeeklyProgressCard(progress = randomProgress)

                // Botón para abrir el pop-up de actividades recientes
                Button(
                    onClick = { showActivitiesDialog = true },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Ver Actividades Recientes")
                }
            }

            // Botón de cerrar sesión con confirmación
            Button(
                onClick = {
                    showLogoutDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                contentPadding = PaddingValues(vertical = 12.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "Cerrar Sesión")
                Spacer(modifier = Modifier.size(8.dp))
                Text("Cerrar Sesión")
            }
        }

        // Diálogo de confirmación para cerrar sesión
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val editor = sharedPreferences.edit()
                        editor.remove("username")
                        editor.putBoolean("login_message_shown", false) // Restablece para la próxima vez
                        editor.apply()
                        navController.navigate("User_Input_Screen")
                        showLogoutDialog = false
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("No")
                    }
                },
                title = { Text("Cerrar Sesión") },
                text = { Text("¿Estás seguro de que deseas cerrar sesión?") }
            )
        }

        // Diálogo de Actividades Recientes con actividades aleatorias
        if (showActivitiesDialog) {
            AlertDialog(
                onDismissRequest = { showActivitiesDialog = false },
                confirmButton = {
                    TextButton(onClick = { showActivitiesDialog = false }) {
                        Text("Cerrar")
                    }
                },
                title = { Text("Actividades Recientes") },
                text = {
                    LazyColumn {
                        items(randomActivities) { activity ->
                            ActivityItem(activity)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun UserProfile(username: String, imageUrl: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Icono de perfil",
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¡Hola, $username!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun AnimatedButton(onClick: () -> Unit, text: String, icon: ImageVector) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun WeeklyProgressCard(progress: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tu progreso semanal", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.size(8.dp))
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.size(4.dp))
            Text("${(progress * 100).toInt()}% completado", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ActivityItem(activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(activity.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

data class Activity(val description: String)
