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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.maccs.events.R
import com.maccs.events.data.local.AppDatabase
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.theme.*

class ProfileActivity : ComponentActivity() {

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.onImageSelected(uri) }
    )

    Scaffold(
        containerColor = Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        color = LightPurple,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    // Icono de Ajustes a la derecha del todo
                    IconButton(onClick = { /* Lógica de ajustes */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- SECCIÓN FOTO DE PERFIL CON BOTÓN DE EDICIÓN ---
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                // El Avatar
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = if (viewModel.isEditable) LightPurple else White,
                            shape = CircleShape
                        )
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
                            modifier = Modifier.size(90.dp),
                            tint = White
                        )
                    }
                }

                // BOTÓN DE EDICIÓN GLOBAL: Al pulsarlo, cambia el estado isEditable de todo el ViewModel
                SmallFloatingActionButton(
                    onClick = { viewModel.toggleEdit() },
                    containerColor = if (viewModel.isEditable) Color.Red else LightPurple,
                    contentColor = White,
                    shape = CircleShape,
                    modifier = Modifier.offset(x = (-8).dp, y = (-8).dp) // Ajuste fino de posición
                ) {
                    Icon(
                        imageVector = if (viewModel.isEditable) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = "Alternar modo edición",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- FORMULARIO (Se habilitan/deshabilitan todos con viewModel.isEditable) ---
            ProfileTextField(
                label = "Nombre",
                value = viewModel.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                isEnabled = viewModel.isEditable,
                borderColor = LightPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(
                label = "Email",
                value = viewModel.mail,
                onValueChange = { viewModel.onMailChange(it) },
                isEnabled = viewModel.isEditable,
                borderColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(
                label = "ID (no editable)",
                value = viewModel.idNoEditable,
                onValueChange = {},
                isEnabled = false, // Este siempre se queda falso
                borderColor = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- BOTÓN GUARDAR (Solo visible cuando isEditable es True) ---
            if (viewModel.isEditable) {
                Button(
                    onClick = { viewModel.guardarPerfil() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .width(130.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, LightPurple)
                ) {
                    Text("Guardar", color = White, fontFamily = NunitoFamily, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(40.dp))

            // --- CERRAR SESIÓN ---
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
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Red),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = White)
            ) {
                Text("Cerrar sesión", fontSize = 18.sp, fontFamily = NunitoFamily, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
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
        label = { Text(label, fontFamily = NunitoFamily) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LightPurple,
            unfocusedBorderColor = if (isEnabled) borderColor else Color.DarkGray,
            disabledBorderColor = Color.DarkGray,
            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledTextColor = Color.Gray,
            focusedLabelColor = LightPurple,
            unfocusedLabelColor = Color.Gray,
            cursorColor = LightPurple
        )
    )
}