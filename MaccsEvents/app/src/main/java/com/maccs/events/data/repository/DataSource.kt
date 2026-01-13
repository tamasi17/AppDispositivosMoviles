package com.maccs.events.data.repository

import com.maccs.events.data.model.User
import com.maccs.events.data.model.Event

interface DataSource {
    fun login(email: String, pass: String, callback: (Boolean) -> Unit)
    fun getEvents(callback: (List<Event>) -> Unit)
    fun createEvent(event: Event, callback: (Boolean) -> Unit)
}