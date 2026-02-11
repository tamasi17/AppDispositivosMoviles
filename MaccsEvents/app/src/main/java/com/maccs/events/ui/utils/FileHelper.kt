package com.maccs.events.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File

object FileHelper {
    fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            // Creamos el archivo en la carpeta "files" interna de la app
            val outputFile = File(context.filesDir, "$fileName.jpg")

            inputStream?.use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            outputFile.absolutePath // Devolvemos la ruta para guardarla en Room
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}