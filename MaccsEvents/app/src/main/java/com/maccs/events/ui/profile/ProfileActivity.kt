package com.maccs.events.ui.profile

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maccs.events.R
import com.maccs.events.ui.theme.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import android.net.Uri
import androidx.compose.foundation.background
import com.maccs.events.ui.components.AppBottomBar
import androidx.compose.ui.tooling.preview.Preview

class ProfileViewModel : ViewModel() {
    var nombre by mutableStateOf("")
    var mail by mutableStateOf("")
    val idNoEditable by mutableStateOf("USER-12345")
    var imageUri by mutableStateOf<Uri?>(null)

    fun onNombreChange(newValue: String) { nombre = newValue }
    fun onMailChange(newValue: String) { mail = newValue }
    fun onImageSelected(uri: Uri?) { imageUri = uri }
    fun guardarPerfil() { println("Guardando: $nombre") }
    fun cerrarSesion(onSuccess: () -> Unit) {
        // En el futuro aquí irá Firebase.auth.signOut()
        println("Sesión cerrada")
        onSuccess()
    }
}
class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileVm: ProfileViewModel = viewModel()
            MaccsEventsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Black,
                    bottomBar = { AppBottomBar() }
                ) { innerPadding ->
                    // AHORA SÍ: pasamos el padding al modifier
                    ProfileScreen(
                        viewModel = profileVm,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current


    // código que abre la galeria para añadir foto
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.onImageSelected(uri) }
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título "Mi Perfil"
        Text(
            text = "Mi Perfil",
            color = LigthPurple,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 32.dp)
        )

        // Imagen de perfil circular
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape) // Corta la imagen en círculo
                .border(2.dp, White, CircleShape)
                .clickable {
                    // Al pulsar, abrimos la galería (solo imágenes)
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.imageUri != null) {
                // Si hay una imagen seleccionada, la mostramos con Coil
                AsyncImage(
                    model = viewModel.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Si no, mostramos el icono por defecto
                Icon(
                    painter = painterResource(id = R.drawable.profile_icon_svg),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = White
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Campo: Nombre
        ProfileTextField(
            label = "Nombre",
            value = viewModel.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            isEnabled = true,
            borderColor = LigthPurple
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Mail
        ProfileTextField(
            label = "Mail",
            value = viewModel.mail,
            onValueChange = { viewModel.onMailChange(it) },
            isEnabled = true,
            borderColor = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: ID (No editable)
        ProfileTextField(
            label = "ID (no editable)",
            value = viewModel.idNoEditable,
            onValueChange = {},
            isEnabled = false,
            borderColor = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón Guardar (Alineado a la derecha)
        Button(
            onClick = { /* Lógica de guardado */ },
            modifier = Modifier
                .align(Alignment.End)
                .width(120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, LigthPurple)
        ) {
            Text("Guardar", color = White)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Cerrar Sesión (Abajo con borde rojo)
        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion {
                    // Esto cierra la Activity y vuelve a la anterior
                    (context as? Activity)?.finish()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = White)
        ) {
            Text("Cerrar sesión", fontSize = 18.sp)
        }
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean,
    borderColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth(),

        label = { Text(label) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LigthPurple,
            unfocusedBorderColor = if (isEnabled) borderColor else Color.DarkGray,
            disabledBorderColor = Color.DarkGray,
            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledTextColor = Color.Gray,
            focusedLabelColor = LigthPurple, // El color del texto pequeño arriba
            unfocusedLabelColor = Color.Gray,
            cursorColor = LigthPurple
        )
    )
}

// --- PREVIEWS PARA PERFIL ---

@Preview(showBackground = true, name = "Perfil - Vista Real")
@Composable
fun PreviewProfileDirecta() {
    // 1. Creamos la instancia manualmente (sin usar viewModel())
    // Esto evita que busque la base de datos o el motor de Compose
    val fakeVm = remember {
        ProfileViewModel().apply {
            nombre = "Juan Pérez"
            mail = "juan.perez@example.com"
        }
    }

    MaccsEventsTheme(darkTheme = true) {
        // 2. Forzamos el Scaffold y la Surface a Negro para que no salga blanco
        Scaffold(
            containerColor = Color.Black,
            bottomBar = {
                // Representación visual de tu AppBottomBar
                Surface(
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    color = Color(0xFF121212)
                ) {
                    Text("Barra de Navegación",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        fontFamily = NunitoFamily)
                }
            }
        ) { padding ->
            Surface(
                modifier = Modifier.fillMaxSize().padding(padding),
                color = Color.Black
            ) {
                // Llamamos a tu función original sin cambiarle nada
                ProfileScreen(viewModel = fakeVm)
            }
        }
    }
}