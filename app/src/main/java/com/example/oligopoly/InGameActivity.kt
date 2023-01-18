package com.example.oligopoly

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.oligopoly.enums.Chance
import com.example.oligopoly.enums.Color
import com.example.oligopoly.enums.CommunityChest
import com.example.oligopoly.services.SessionService
import com.example.oligopoly.interfaces.Field
import com.example.oligopoly.models.*

class InGameActivity : AppCompatActivity() {
    private lateinit var sessionService: SessionService
    private lateinit var board: Array<Field>
    private lateinit var currentPlayer: Player
    private lateinit var players: Array<Player>
    private lateinit var propertySets: Array<Array<Property>>

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
        // TODO: set players from GameSettingsActivity
        initGame()
    }

    override fun onStop() {
        sessionService.saveSession(Session(players)) // Session class is currently useless, but could eventually contain more values
        super.onStop()
    }

    override fun onPause() {
        sessionService.saveSession(Session(players))
        super.onPause()
    }

    private fun initGame() {
        val startField = StartField("STA")

        val firstDoNothingField = DoNothingField("DON1")
        val secondDoNothingField = DoNothingField("DON2")
        val thirdDoNothingField =
            DoNothingField("DON3")    // 3 of these are corners and the other two pay 100 or 200 tax in real monopoly
        val fourthDoNothingField = DoNothingField("DON4")
        val fifthDoNothingField = DoNothingField("DON5")

        val firstChance = StartField("CHA1")
        val secondChance = StartField("CHA2")
        val thirdChance = StartField("CHA3")
        val fourthChance = StartField("CHA4")
        // water and electricity replaced with chance and community chest
        val firstCommunityChest = CommunityChestField("COM1")
        val secondCommunityChest = CommunityChestField("COM2")
        val thirdCommunityChest = CommunityChestField("COM3")
        val fourthCommunityChest = CommunityChestField("COM4")

        val firstBrownProperty = Property("BRO1", Color.Brown, false, null, 60, 10, 50)
        val secondBrownProperty = Property("BRO2", Color.Brown, false, null, 60, 15, 60)
        val brownPropertySet = arrayOf(firstBrownProperty, secondBrownProperty)

        val firstCyanProperty = Property("CYA1", Color.Cyan, false, null, 100, 20, 80)
        val secondCyanProperty = Property("CYA2", Color.Cyan, false, null, 100, 20, 80)
        val thirdCyanProperty = Property("CYA3", Color.Cyan, false, null, 110, 30, 100)
        val cyanPropertySet = arrayOf(firstCyanProperty, secondCyanProperty, thirdCyanProperty)

        val firstMagentaProperty = Property("MAG1", Color.Magenta, false, null, 140, 30, 100)
        val secondMagentaProperty = Property("MAG2", Color.Magenta, false, null, 140, 30, 100)
        val thirdMagentaProperty = Property("MAG3", Color.Magenta, false, null, 160, 45, 125)
        val magentaPropertySet =
            arrayOf(firstMagentaProperty, secondMagentaProperty, thirdMagentaProperty)

        val firstOrangeProperty = Property("ORA1", Color.Orange, false, null, 180, 60, 150)
        val secondOrangeProperty = Property("ORA2", Color.Orange, false, null, 180, 60, 150)
        val thirdOrangeProperty = Property("ORA3", Color.Orange, false, null, 200, 80, 180)
        val orangePropertySet =
            arrayOf(firstOrangeProperty, secondOrangeProperty, thirdOrangeProperty)

        val firstRedProperty = Property("RED1", Color.Red, false, null, 220, 90, 190)
        val secondRedProperty = Property("RED2", Color.Red, false, null, 220, 90, 190)
        val thirdRedProperty = Property("RED3", Color.Red, false, null, 240, 115, 225)
        val redPropertySet = arrayOf(firstRedProperty, secondRedProperty, thirdRedProperty)

        val firstYellowProperty = Property("YEL1", Color.Yellow, false, null, 260, 120, 220)
        val secondYellowProperty = Property("YEL2", Color.Yellow, false, null, 260, 120, 220)
        val thirdYellowProperty = Property("YEL3", Color.Yellow, false, null, 280, 150, 260)
        val yellowPropertySet =
            arrayOf(firstYellowProperty, secondYellowProperty, thirdYellowProperty)

        val firstGreenProperty = Property("GRE1", Color.Green, false, null, 300, 170, 290)
        val secondGreenProperty = Property("GRE2", Color.Green, false, null, 300, 170, 290)
        val thirdGreenProperty = Property("GRE3", Color.Green, false, null, 320, 205, 335)
        val greenPropertySet = arrayOf(firstGreenProperty, secondGreenProperty, thirdGreenProperty)

        val firstBlueProperty = Property("BLU1", Color.Blue, false, null, 350, 280, 450)
        val secondBlueProperty = Property("BLU2", Color.Blue, false, null, 400, 340, 550)
        val bluePropertySet = arrayOf(firstBlueProperty, secondBlueProperty)

        val firstTrainProperty = Property("TRA1", Color.Black, false, null, 200, 50, 300)
        val secondTrainProperty = Property("TRA2", Color.Black, false, null, 200, 50, 300)
        val thirdTrainProperty = Property("TRA3", Color.Black, false, null, 200, 50, 300)
        val fourthTrainProperty = Property("TRA4", Color.Black, false, null, 200, 50, 300)
        val trainPropertySet = arrayOf(
            firstTrainProperty,
            secondTrainProperty,
            thirdTrainProperty,
            fourthTrainProperty
        )

        board = arrayOf(
            startField,
            firstBrownProperty,
            firstCommunityChest,
            secondBrownProperty,
            firstDoNothingField,
            firstTrainProperty,
            firstCyanProperty,
            firstChance,
            secondCyanProperty,
            thirdCyanProperty,
            secondDoNothingField,
            firstMagentaProperty,
            secondCommunityChest,
            secondMagentaProperty,
            thirdMagentaProperty,
            secondTrainProperty,
            firstOrangeProperty,
            secondChance,
            secondOrangeProperty,
            thirdOrangeProperty,
            thirdDoNothingField,
            firstRedProperty,
            thirdCommunityChest,
            secondRedProperty,
            thirdRedProperty,
            thirdTrainProperty,
            firstYellowProperty,
            secondYellowProperty,
            thirdChance,
            thirdYellowProperty,
            fourthDoNothingField,
            firstGreenProperty,
            secondGreenProperty,
            fourthCommunityChest,
            thirdGreenProperty,
            fourthTrainProperty,
            fourthChance,
            firstBlueProperty,
            fifthDoNothingField,
            secondBlueProperty
        )

        propertySets = arrayOf(
            brownPropertySet,
            cyanPropertySet,
            magentaPropertySet,
            orangePropertySet,
            redPropertySet,
            yellowPropertySet,
            greenPropertySet,
            bluePropertySet,
            trainPropertySet
        )
    }

    //TODO: add function to save session every 30s

    public fun rollDice() {
        val firstDice = getRandomDiceRoll()
        val secondDice = getRandomDiceRoll()

        // TODO play bad animation of dice rolling (use one of 6 dice animations twice based on rolled number)

        movePlayerBy(firstDice + secondDice)
        handleActionOnLandedField()

        changeTurn()
    }

    private fun promptPropertyPurchase() {
        TODO("implement this")
    }

    private fun getRandomDiceRoll(): Int {
        return (1..6).random()
    }

    private fun isPropertyASet(p: Property, otherTeam: Team): Boolean {
        val propertySet = propertySets.find { properties -> properties.contains(p) }

        propertySet!!.forEach {
            if (!it.isPurchased || it.ownedBy != otherTeam) {
                return false
            }
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    private fun transferBalance(from: Team, to: Team, amount: Int) {
        from.balance -= amount
        to.balance += amount
        from.balanceTextView!!.text = "${from.balance}$"
        to.balanceTextView!!.text = "${to.balance}$"

        declareBankruptcyIfTeamIsBankrupt(from)
    }

    @SuppressLint("SetTextI18n")
    private fun addBalance(to: Team, amount: Int) {
        to.balance += amount
        to.balanceTextView!!.text = "${to.balance}$"
    }

    @SuppressLint("SetTextI18n")
    private fun subtractBalance(from: Team, amount: Int) {
        from.balance -= amount
        from.balanceTextView!!.text = "${from.balance}$"

        declareBankruptcyIfTeamIsBankrupt(from)
    }

    private fun declareBankruptcyIfTeamIsBankrupt(t: Team) {
        if(t.balance < 0) {
            handleBankruptcy()
        }
    }

    private fun handleBankruptcy() {
        sendGameResultNotification()
        sessionService.discardSession()
        startActivity(Intent(this, MainActivity::class.java))
    }

    @SuppressLint("SetTextI18n")
    private fun addPropertyToCurrentTeam(p: Property) {
        currentPlayer.team.ownedProperties.add(p)
        p.purchase(currentPlayer.team)
        val newPropertyText = if (currentPlayer.team.propertiesTextView!!.text.toString()
                .isEmpty()
        ) p.fieldName else ", ${p.fieldName}"
        currentPlayer.team.propertiesTextView!!.text =
            currentPlayer.team.propertiesTextView!!.text.toString() + newPropertyText
    }

    private fun movePlayerBy(by: Int) {
        val boardIndexOfNewLocation = board.indexOf(currentPlayer.position) + by
        currentPlayer.position = board[boardIndexOfNewLocation]
        currentPlayer.positionTextView!!.text = currentPlayer.position.fieldName

        if (wasStartFieldPassed(boardIndexOfNewLocation, by)) {
            addBalance(currentPlayer.team, 200)
        }
    }

    private fun handleActionOnLandedField() {
        if (currentPlayer.position is Property) {
            val currentProperty = currentPlayer.position as Property

            if (currentProperty.isPurchased) {
                if (isPropertyASet(currentProperty, getOtherTeam())) {
                    transferBalance(currentPlayer.team, getOtherTeam(), currentProperty.setFee)
                } else {
                    transferBalance(currentPlayer.team, getOtherTeam(), currentProperty.fee)
                }
            } else {
                if (currentPlayer.team.balance >= currentProperty.cost) {
                    //TODO: Prompt user to buy or not with dialog (promptPropertyPurchase)
                    val userWantsToBuy = true

                    if (userWantsToBuy) {
                        subtractBalance(currentPlayer.team, currentProperty.cost)
                        addPropertyToCurrentTeam(currentProperty)
                    }
                }
            }
        } else if (currentPlayer.position is ChanceField) {
            executeChance(currentPlayer.position as ChanceField)
        } else if (currentPlayer.position is CommunityChestField) {
            executeCommunityChest(currentPlayer.position as CommunityChestField)
        }
    }

    private fun executeChance(c: ChanceField) {
        when (c.getRandomChance()) {
            Chance.GoToStart -> currentPlayer.position = board[0]
            Chance.ThreeSpacesForward -> movePlayerBy(3)
            Chance.StealOneHundred -> transferBalance(getOtherTeam(), currentPlayer.team, 100)
            Chance.ThreeSpacesBack -> movePlayerBy(-3)
        }
        //TODO: Add dialog showing what they got
    }

    private fun executeCommunityChest(cc: CommunityChestField) {
        when (cc.getRandomCommunityChestField()) {
            CommunityChest.EarnFifty -> addBalance(currentPlayer.team, 50)
            CommunityChest.PayFifty -> transferBalance(currentPlayer.team, getOtherTeam(), 50)
            CommunityChest.EarnTwoHundred -> addBalance(currentPlayer.team, 200)
            CommunityChest.PayTwoHundred -> transferBalance(currentPlayer.team, getOtherTeam(), 200)
            CommunityChest.PayTwentyForEveryProperty -> transferBalance(
                currentPlayer.team,
                getOtherTeam(),
                currentPlayer.team.ownedProperties.size * 20
            )
        }
        // TODO: Add dialog showing what they got
    }

    private fun wasStartFieldPassed(lastIndex: Int, currentIndex: Int): Boolean {
        return currentIndex - lastIndex < 0
    }

    private fun sendGameResultNotification() {
        TODO("implement this")
    }

    private fun changeTurn() {
        val currentPlayerIndex = players.indexOf(currentPlayer)

        currentPlayer = if (currentPlayerIndex < 3) {
            players[currentPlayerIndex + 1]
        } else {
            players[0]
        }
    }

    private fun getOtherTeam(): Team {
        return players.find { p -> p.team != currentPlayer.team }!!.team
    }
}