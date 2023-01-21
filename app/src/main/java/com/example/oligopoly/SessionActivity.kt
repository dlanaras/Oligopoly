package com.example.oligopoly

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import com.example.oligopoly.services.SessionService

class SessionActivity : AppCompatActivity() {
    private lateinit var sessionService: SessionService
    private lateinit var resumeButton: Button
    private lateinit var discardButton: Button
    private var mBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as SessionService.LocalBinder
            sessionService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        Intent(this, SessionService::class.java).also { intent ->
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        resumeButton = findViewById(R.id.resumeButton)
        discardButton = findViewById(R.id.discardButton)

        resumeButton.setOnClickListener {
            resumeSession()
        }

        discardButton.setOnClickListener {
            discardSessionAndNavigateToMainActivity()
        }
    }

    private fun discardSessionAndNavigateToMainActivity() {
        if(mBound) {
            sessionService.discardSession()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun resumeSession() {
        if(mBound) {
            val resumeIntent = Intent(this, InGameActivity::class.java)
            resumeIntent.putExtra("resumeGame", true)

            startActivity(resumeIntent)
        }
    }
}