package com.example.oligopoly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.oligopoly.enums.Chance
import com.example.oligopoly.enums.Color
import com.example.oligopoly.enums.CommunityChest
import com.example.oligopoly.models.Player
import com.example.oligopoly.models.Property
import com.example.oligopoly.models.Session
import com.example.oligopoly.models.Team
import com.example.oligopoly.services.SessionService
import org.w3c.dom.Text
import java.lang.reflect.Field

class InGameActivity : AppCompatActivity() {
    private lateinit var sessionService: SessionService
    private lateinit var board: Array<Field>
    private lateinit var currentPlayer: Player
    private lateinit var players: Array<Player>

    private lateinit var rollDiceButton: Button
    private lateinit var teamABalanceText: TextView
    private lateinit var teamBBalanceText: TextView
    private lateinit var teamAPropertiesText: TextView
    private lateinit var teamBPropertiesText: TextView
    private lateinit var teamANameText: TextView
    private lateinit var teamBNameText: TextView
    private lateinit var playerANameText: TextView
    private lateinit var playerBNameText: TextView
    private lateinit var playerCNameText: TextView
    private lateinit var playerDNameText: TextView
    private lateinit var playerAPositionText: TextView
    private lateinit var playerBPositionText: TextView
    private lateinit var playerCPositionText: TextView
    private lateinit var playerDPositionText: TextView
    //TODO: Add Dialog for buying properties

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_game)
    }

    override fun onStop() {
        sessionService.saveSession(Session(players)) // Session class is currently useless, but could eventually contain more values
        super.onStop()
    }

    override fun onPause() {
        sessionService.saveSession(Session(players))
        super.onPause()
    }

    public fun rollDice() {
        TODO("implement this")
    }

    private fun promptPropertyPurchase() {
        TODO("implement this")
    }

    private fun getRandomDiceRoll(): Int {
        TODO("implement this")
    }

    private fun transferBalance(from: Team, to: Team, amount: Int) {
        TODO("implement this")
    }

    private fun addBalance(to: Team, amount: Int) {
        TODO("implement this")
    }

    private fun handleBankruptcy() {
        TODO("implement this")
    }

    private fun movePlayerBy(player: Player, by: Int) {
        TODO("implement this")
    }

    private fun executeChance(c: Chance) {
        TODO("implement this")
    }

    private fun executeCommunityChest(cc: CommunityChest) {
        TODO("implement this")
    }

    private fun wasStartFieldPassed(currentRoll: Int, player: Player): Boolean {
        TODO("implement this")
    }

    private fun showGameResultNotification() {
        TODO("implement this")
    }

    private fun changeTurn() {
        TODO("implement this")
    }
}