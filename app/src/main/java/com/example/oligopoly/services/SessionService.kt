package com.example.oligopoly.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.oligopoly.models.Session
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class SessionService : Service() {
    private val fileName = "oligopoly_session"
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService() : SessionService = this@SessionService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    public fun saveSession(session: Session) {
        this.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(Json.encodeToString(session).toByteArray())
        }
    }

    public fun getSession(): Session {
        this.openFileInput(fileName).use {
            return Json.decodeFromString(it.readBytes().toString())
        }
    }

    public fun discardSession() {
        this.deleteFile(fileName)
    }
}