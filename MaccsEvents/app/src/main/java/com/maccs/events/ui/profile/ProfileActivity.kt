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
import androidx.compose.ui.text.TextStyle
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
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileVm: ProfileViewModel = viewModel()
            MaccsEventsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Black,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Mi Perfil",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = NunitoFamily,
                                        color = LigthPurple,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Black,
                                titleContentColor = LigthPurple
                            )
                        )
                    },
                    bottomBar = { AppBottomBar() }
                ) { innerPadding ->
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

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.onImageSelected(uri) }
    )

    // Usamos una Column con scroll por si los campos no caben en pantallas pequeñas
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Imagen de perfil circular
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(2.dp, White, CircleShape)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.imageUri != null) {
                AsyncImage(
                    model = viewModel.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.profile_icon_svg),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = White
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        ProfileTextField(
            label = "Nombre",
            value = viewModel.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            isEnabled = true,
            borderColor = LigthPurple
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(
            label = "Email",
            value = viewModel.mail,
            onValueChange = { viewModel.onMailChange(it) },
            isEnabled = true,
            borderColor = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(
            label = "ID (no editable)",
            value = viewModel.idNoEditable,
            onValueChange = {},
            isEnabled = false,
            borderColor = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.guardarPerfil() },
            modifier = Modifier
                .align(Alignment.End)
                .width(130.dp)
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LigthPurple,
                contentColor = Black
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("GUARDAR", style = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold))
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion {
                    (context as? Activity)?.finish()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = White)
        ) {
            Text("CERRAR SESIÓN", fontSize = 18.sp, fontFamily = NunitoFamily)
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

        textStyle = TextStyle(fontFamily = NunitoFamily),
        label = { Text(label, fontFamily = NunitoFamily) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Perfil - Diseño Final")
@Composable
fun PreviewProfileFinal() {
    val fakeVm = remember {
        ProfileViewModel().apply {
            nombre = "Juan Pérez"
            mail = "juan.perez@example.com"
        }
    }

    MaccsEventsTheme(darkTheme = true) {
        Scaffold(
            containerColor = Color.Black,
            topBar = {
                TopAppBar(
                    title = {
                        Text("Mi Perfil",
                            style = TextStyle(
                                fontFamily = NunitoFamily,
                                color = LigthPurple,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            bottomBar = {
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
            ProfileScreen(
                viewModel = fakeVm,
                modifier = Modifier.padding(padding)
            )
        }
    }
}