import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // Definimos los estados para cada campo
    var nombre by mutableStateOf("")
    var mail by mutableStateOf("")
    val idNoEditable by mutableStateOf("USER-12345") // Hardcoded por ahora

    fun onNombreChange(newValue: String) {
        nombre = newValue
    }

    fun onMailChange(newValue: String) {
        mail = newValue
    }

    fun guardarPerfil() {
        // Aquí irá la lógica para guardar en Firebase o base de datos local
        println("Guardando perfil: $nombre - $mail")
    }
}