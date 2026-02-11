import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    // --- CAMBIOS AQUÍ ---

    // 1. El ID sigue siendo automático (es el "DNI" del usuario en Firebase)
    val idNoEditable by mutableStateOf(currentUser?.uid ?: "Sin ID")

    // 2. Nombre y Mail ahora empiezan vacíos (""), no se traen de Firebase
    var nombre by mutableStateOf("")
    var mail by mutableStateOf("")

    // 3. La imagen empieza en null (vacía)
    var imageUri by mutableStateOf<Uri?>(null)

    // --- EL RESTO SE MANTIENE IGUAL ---

    fun onNombreChange(newValue: String) {
        nombre = newValue
    }

    fun onMailChange(newValue: String) {
        mail = newValue
    }

    fun onImageSelected(uri: Uri?) {
        imageUri = uri
    }

    fun guardarPerfil() {
        // Al guardar, usaremos el idNoEditable para saber a qué usuario
        // pertenecen el nombre y mail que ha escrito
        println("Guardando perfil para ID ${idNoEditable}: $nombre - $mail")
    }

    fun cerrarSesion(onSuccess: () -> Unit) {
        auth.signOut()
        println("Sesión cerrada en Firebase")
        onSuccess()
    }
}