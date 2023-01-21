package com.example.oligopoly

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import com.example.oligopoly.services.SessionService

class MainActivity : AppCompatActivity() {
    private lateinit var playButton: Button
    private var mBound = false
    private lateinit var sessionService: SessionService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as SessionService.LocalBinder
            sessionService = binder.getService()
            mBound = true

            if(sessionService.sessionExists()) {
                startActivity(Intent(this@MainActivity, SessionActivity::class.java))
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Intent(this, SessionService::class.java).also { intent ->
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        playButton = findViewById(R.id.playButton)

        playButton.text = "Play"

        playButton.setOnClickListener() {
            startActivity(Intent(this, GameSettingsActivity::class.java))
        }
    }
}