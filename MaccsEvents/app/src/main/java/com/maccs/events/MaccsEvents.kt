package com.maccs.events

import android.app.Application
import androidx.room.Room
import com.maccs.events.data.local.AppDatabase

class MaccsEventsApp : Application() {

    // "companion object" is like "static" in Java.
    // It allows you to access the database from ANYWHERE in your code
    // simply by typing: MaccsEventsApp.database
    companion object {
        lateinit var database: AppDatabase
    }

    // onCreate runs ONLY ONCE when the app launches.
    override fun onCreate() {
        super.onCreate()

        // We build the database here so it's ready before any screen loads.
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "maccs-events-db"
        ).build()
    }
}