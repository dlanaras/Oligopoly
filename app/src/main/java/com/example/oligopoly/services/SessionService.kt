package com.example.oligopoly.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.oligopoly.models.Session
import com.example.oligopoly.models.format
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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

    fun saveSession(session: Session) {
        this.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(format.encodeToString(session).toByteArray())
        }
    }

    fun getSession(): Session {
        val fileContents = this.openFileInput(fileName).bufferedReader().use { it.readText() }
        return format.decodeFromString(fileContents)
    }

    fun discardSession() {
        this.deleteFile(fileName)
    }

    // TODO: use in main activity to check when to navigate to sessionActivity
    fun sessionExists() : Boolean {
        return File(fileName).exists()
    }
}