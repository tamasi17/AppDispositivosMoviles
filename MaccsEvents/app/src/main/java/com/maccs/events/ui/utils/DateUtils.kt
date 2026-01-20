package com.maccs.events.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Función de extensión: Ahora cualquier Long puede convertirse a texto
fun Long.toReadableDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}