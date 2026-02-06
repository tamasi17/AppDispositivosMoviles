package com.maccs.events.ui.profile

import ProfileViewModel
import android.app.Activity
import android.content.Intent // Importante para cambiar de pantalla
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
// Importa tu LoginActivity (ajusta el package si es necesario)
import com.maccs.events.ui.auth.LoginActivity
import com.maccs.events.ui.components.AppBottomBar

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Usamos el ViewModel que ya tiene la lógica de Firebase
            val profileVm: ProfileViewModel = viewModel()
            MaccsEventsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Black,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Perfil",
            color = LigthPurple,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 32.dp)
        )

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

        ProfileTextField("Nombre", viewModel.nombre, { viewModel.onNombreChange(it) }, true, LigthPurple)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Mail", viewModel.mail, { viewModel.onMailChange(it) }, true, Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("ID (no editable)", viewModel.idNoEditable, {}, false, Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.guardarPerfil() },
            modifier = Modifier.align(Alignment.End).width(120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, LigthPurple)
        ) {
            Text("Guardar", color = White)
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
fun ProfileTextField(label: String, value: String, onValueChange: (String) -> Unit, isEnabled: Boolean, borderColor: Color) {
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
            focusedLabelColor = LigthPurple,
            unfocusedLabelColor = Color.Gray,
            cursorColor = LigthPurple
        )
    )
}