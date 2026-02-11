package com.maccs.events.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.maccs.events.R
import com.maccs.events.ui.theme.*

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, onSuccess: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }
    val state = viewModel.uiState

    LaunchedEffect(state) {
        if (state is RegisterUiState.Success) onSuccess()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Black).padding(32.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.height(100.dp).fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Selector de foto circular
        Box(
            modifier = Modifier.size(120.dp).clip(CircleShape).background(White.copy(0.1f))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Crop)
            } else {
                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = LigthPurple)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Campos usando el estilo de tu login
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LigthPurple, unfocusedTextColor = White, focusedTextColor = White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LigthPurple, unfocusedTextColor = White, focusedTextColor = White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LigthPurple, unfocusedTextColor = White, focusedTextColor = White)
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (state is RegisterUiState.Loading) {
            CircularProgressIndicator(color = LigthPurple)
        } else {
            Button(
                onClick = { viewModel.onRegister(context, email, password, name, imageUri) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LigthPurple)
            ) {
                Text("REGISTRARSE", fontWeight = FontWeight.Bold)
            }
        }
    }
}