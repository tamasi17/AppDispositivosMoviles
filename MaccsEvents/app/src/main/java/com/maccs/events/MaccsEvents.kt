package com.maccs.events

import android.app.Application
import com.maccs.events.data.AppContainer
import com.maccs.events.data.DefaultAppContainer

class MaccsEventsApp : Application() {

    // Esta variable guardará el contenedor de dependencias
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializamos el contenedor
        // Esto crea la base de datos y el repositorio
        container = DefaultAppContainer(this)
    }
}