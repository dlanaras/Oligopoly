package com.example.oligopoly.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.oligopoly.models.Session

class SessionService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    public fun saveSession(session: Session) {
        TODO("Implement this")
    }

    public fun getSession(): Session {
        TODO("Implement this")
    }

    public fun discardSession() {
        TODO("Not yet implemented")
    }
}