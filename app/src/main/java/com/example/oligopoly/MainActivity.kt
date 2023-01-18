package com.example.oligopoly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var playButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton = findViewById(R.id.playButton);

        playButton.text = "Play"

        playButton.setOnClickListener() {
            startActivity(Intent(this, GameSettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
    }
}