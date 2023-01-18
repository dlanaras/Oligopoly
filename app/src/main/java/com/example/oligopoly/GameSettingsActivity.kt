package com.example.oligopoly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.oligopoly.models.Player

class GameSettingsActivity : AppCompatActivity() {
    private lateinit var teamANameInput: EditText
    private lateinit var teamBNameInput: EditText
    private lateinit var playerANameInput: EditText
    private lateinit var playerBNameInput: EditText
    private lateinit var playerCNameInput: EditText
    private lateinit var playerDNameInput: EditText
    private lateinit var startingBalanceInput: EditText
    private lateinit var startButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        startButton = findViewById(R.id.startButton)
        backButton = findViewById(R.id.backButton)

        startButton.setOnClickListener() {
            startActivity(Intent(this, InGameActivity::class.java))
        }

        backButton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}