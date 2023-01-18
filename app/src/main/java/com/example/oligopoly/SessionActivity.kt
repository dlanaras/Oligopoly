package com.example.oligopoly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.oligopoly.services.SessionService

class SessionActivity : AppCompatActivity() {
    private lateinit var sessionService: SessionService
    private lateinit var resumeButton: Button
    private lateinit var discardButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)
    }

    public fun discardSessionAndNavigateToMainActivity() {
        TODO("implement this")
    }

    public fun resumeSession() {
        TODO("implement this")
    }
}