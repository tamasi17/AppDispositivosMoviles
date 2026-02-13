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
import androidx.compose.ui.tooling.preview.Preview
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Settings - No hace nada */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = White)
            }

            Text(
                text = "Mi Perfil",
                color = LightPurple,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = NunitoFamily)
            )

            IconButton(onClick = { viewModel.toggleEdit() }) {
                Icon(
                    imageVector = if (viewModel.isEditable) Icons.Default.Close else Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = if (viewModel.isEditable) Color.Red else LightPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(2.dp, if (viewModel.isEditable) LightPurple else White, CircleShape)
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

        ProfileTextField("Nombre", viewModel.nombre, { viewModel.onNombreChange(it) }, viewModel.isEditable, LightPurple)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("Mail", viewModel.mail, { viewModel.onMailChange(it) }, viewModel.isEditable, Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextField("ID (no editable)", viewModel.idNoEditable, {}, false, Color.DarkGray)

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isEditable) {
            Button(
                onClick = { viewModel.guardarPerfil() },
                modifier = Modifier.align(Alignment.End).width(120.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Black),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, LightPurple)
            ) {
                Text("Guardar", color = White, fontFamily = NunitoFamily)
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = White)
        ) {
            Text("Cerrar sesión", fontSize = 18.sp, fontFamily = NunitoFamily)
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

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MaccsEventsTheme {
        Text("Vista previa cargando...", color = Color.White, fontFamily = NunitoFamily)
    }
}