package com.example.oligopoly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

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

        teamANameInput = findViewById(R.id.teamANameInput)
        teamBNameInput = findViewById(R.id.teamBNameInput)
        playerANameInput = findViewById(R.id.personANameInput)
        playerBNameInput = findViewById(R.id.personBNameInput)
        playerCNameInput = findViewById(R.id.personCNameInput)
        playerDNameInput = findViewById(R.id.personDNameInput)
        startingBalanceInput = findViewById(R.id.startingBalanceInput)

        startButton = findViewById(R.id.startButton)
        backButton = findViewById(R.id.backButton)
        val bundle = Bundle()


        startButton.setOnClickListener() {
            if(inputsAreValid()) {
                bundle.putString("playerA", playerANameInput.text.toString())
                bundle.putString("playerB", playerBNameInput.text.toString())
                bundle.putString("playerC", playerCNameInput.text.toString())
                bundle.putString("playerD", playerDNameInput.text.toString())
                bundle.putString("teamA", teamANameInput.text.toString())
                bundle.putString("teamB", teamBNameInput.text.toString())
                bundle.putInt("startingBalance", startingBalanceInput.text.toString().toInt())

                val startIntent = Intent(this, InGameActivity::class.java)
                startIntent.putExtras(bundle)

                startActivity(startIntent)
            }
        }

        backButton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun inputsAreValid() : Boolean {
        val stringInputs = listOf(teamANameInput, teamBNameInput, playerANameInput, playerBNameInput, playerCNameInput, playerDNameInput)

        stringInputs.forEach { input ->
            if(input.text.isEmpty() || input.text.length > 16) {
                return false
            }
        }

        if(stringInputs.groupBy { it.text }.size != stringInputs.size) { // values are not unique
            return false
        }

        val oneMillion = 1_000_000
        val startingBalance: Int
        try {
            startingBalance = startingBalanceInput.text.toString().toInt()
        } catch (e: java.lang.NumberFormatException) {
            println("invalid integer entered")
            return false
        }


        return (startingBalanceInput.text.isNotEmpty() && startingBalance <= oneMillion && startingBalance > 0)
    }
}