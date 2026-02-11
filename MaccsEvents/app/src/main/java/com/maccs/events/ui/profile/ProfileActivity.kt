package com.maccs.events.ui.profile

import ProfileViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maccs.events.R
import com.maccs.events.ui.theme.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.ui.text.TextStyle
import com.maccs.events.ui.auth.LoginActivity
import com.maccs.events.ui.components.AppBottomBar
import androidx.compose.ui.tooling.preview.Preview

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
fun ProfileScreen(viewModel: ProfileViewModel, modifier: Modifier = Modifier) {
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

        // Imagen de perfil
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
            label = "ID",
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

        // BOTÓN CERRAR SESIÓN MODIFICADO
        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion {
                    // 1. Preparamos el salto a la pantalla de Login
                    // NOTA: Si tu clase de Login se llama distinto, cambia "LoginActivity" por el nombre correcto
                    val intent = Intent(context, com.maccs.events.ui.auth.LoginActivity::class.java)

                    // 2. Limpiamos el historial para que no pueda volver atrás al perfil
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    context.startActivity(intent)

                    // 3. Cerramos esta pantalla de Perfil definitivamente
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
fun ProfileTextField(label: String, value: String, onValueChange: (String) -> Unit, isEnabled: Boolean, borderColor: Color) {
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
            focusedLabelColor = LigthPurple,
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