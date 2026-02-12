package com.maccs.events.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.ui.text.TextStyle
import com.maccs.events.ui.auth.LoginActivity
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.R
import com.maccs.events.data.local.AppDatabase
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.theme.*
import androidx.compose.foundation.BorderStroke

class ProfileActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(applicationContext)
        val userDao = db.userDao()

        setContent {
            val profileVm: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(userDao)
            )

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
      

       // Mantenemos la fila de botones, pero quitamos el Título (porque ya está en la barra de arriba)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp), // Ajustamos padding ya que hay barra arriba
            horizontalArrangement = Arrangement.SpaceBetween, // Botones a los extremos
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Settings (Izquierda)
            IconButton(onClick = { /* Settings - No hace nada aún */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White) // Asegura Color.White
            }

            // AQUÍ BORRAMOS EL TEXTO "MI PERFIL" PARA QUE NO SALGA DUPLICADO

            // Botón Editar (Derecha) - ¡IMPORTANTE MANTENERLO!
            IconButton(onClick = { viewModel.toggleEdit() }) {
                Icon(
                    imageVector = if (viewModel.isEditable) Icons.Default.Close else Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = if (viewModel.isEditable) Color.Red else LigthPurple
                )
            }
        }


        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(2.dp, if (viewModel.isEditable) LigthPurple else White, CircleShape)
                .clickable(enabled = viewModel.isEditable) {
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

// CAMPO NOMBRE (Estilo de Font + Lógica de Dev)
        ProfileTextField(
            label = "Nombre",
            value = viewModel.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            isEnabled = viewModel.isEditable, // <--- IMPORTANTE: Lógica de Dev
            borderColor = LigthPurple
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CAMPO EMAIL
        ProfileTextField(
            label = "Email",
            value = viewModel.mail,
            onValueChange = { viewModel.onMailChange(it) },
            isEnabled = viewModel.isEditable, // <--- IMPORTANTE
            borderColor = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // CAMPO ID
        ProfileTextField(
            label = "ID (no editable)",
            value = viewModel.idNoEditable,
            onValueChange = {},
            isEnabled = false,
            borderColor = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN GUARDAR
        // Usamos el 'if' de DEV para que solo salga al editar
        if (viewModel.isEditable) {
            Button(
                onClick = { viewModel.guardarPerfil() },
                modifier = Modifier
                    .align(Alignment.End)
                    .width(130.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LigthPurple, // Estilo de Font
                    contentColor = Color.Black    // Estilo de Font
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    "GUARDAR", 
                    style = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion {
                    val intent = Intent(context, com.maccs.events.ui.auth.LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
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

