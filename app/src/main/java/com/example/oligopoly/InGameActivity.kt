package com.example.oligopoly

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.oligopoly.enums.Chance
import com.example.oligopoly.enums.Color
import com.example.oligopoly.enums.CommunityChest
import com.example.oligopoly.interfaces.Field
import com.example.oligopoly.models.*
import com.example.oligopoly.services.SessionService
import java.util.*
import kotlin.collections.ArrayList

class InGameActivity : AppCompatActivity() {
    private var isPurchasing: Boolean = false
    private var resumeGame = false
    private var mBound = false
    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var runnable: Runnable
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

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as SessionService.LocalBinder
            sessionService = binder.getService()
            mBound = true

            if (resumeGame) {
                setPlayersFromSession(sessionService.getSession()!!)
            }

            mainHandler.post(object : Runnable {
                override fun run() {
                    runnable = this
                    sessionService.saveSession(
                        Session(
                            playersToPlayerDtoArray(),
                            PlayerDto.toPlayerDto(currentPlayer)
                        )
                    )
                    mainHandler.postDelayed(this, 30000)
                }
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_game)

        Intent(this, SessionService::class.java).also { intent ->
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        teamABalanceText = findViewById(R.id.teamABalanceText)
        teamBBalanceText = findViewById(R.id.teamBBalanceText)
        teamAPropertiesText = findViewById(R.id.teamAPropertiesText)
        teamBPropertiesText = findViewById(R.id.teamBPropertiesText)
        teamANameText = findViewById(R.id.teamANameText)
        teamBNameText = findViewById(R.id.teamBNameText)
        playerANameText = findViewById(R.id.playerANameText)
        playerBNameText = findViewById(R.id.playerBNameText)
        playerCNameText = findViewById(R.id.playerCNameText)
        playerDNameText = findViewById(R.id.playerDNameText)
        playerAPositionText = findViewById(R.id.playerAPositionText)
        playerBPositionText = findViewById(R.id.playerBPositionText)
        playerCPositionText = findViewById(R.id.playerCPositionText)
        playerDPositionText = findViewById(R.id.playerDPositionText)

        initGame()

        if (intent.getBooleanExtra("resumeGame", false)) {
            resumeGame = true
        } else {
            initPlayers()
        }
    }

    private fun playerDtosToPlayerArray(playerDtos: Array<PlayerDto>): Array<Player> {
        val ps = ArrayList<Player>()

        playerDtos.forEach { playerDto ->
            ps.add(PlayerDto.toPlayer(playerDto))
        }

        ps[0].positionTextView

        return arrayOf(ps[0], ps[1], ps[2], ps[3])
    }

    private fun playersToPlayerDtoArray(): Array<PlayerDto> {
        val ps = ArrayList<PlayerDto>()

        players.forEach { player ->
            ps.add(PlayerDto.toPlayerDto(player))
        }

        return arrayOf(ps[0], ps[1], ps[2], ps[3])
    }

    private fun setPlayersFromSession(session: Session) {
        players = playerDtosToPlayerArray(session.players)

        val playerA = players[0]
        val playerB = players[1]
        val playerC = players[2]
        val playerD = players[3]

        playerA.positionTextView = playerAPositionText
        playerB.positionTextView = playerBPositionText
        playerC.positionTextView = playerCPositionText
        playerD.positionTextView = playerDPositionText

        playerD.team.balanceTextView = teamBBalanceText
        playerD.team.propertiesTextView = teamBPropertiesText
        playerC.team.balanceTextView = teamBBalanceText
        playerC.team.propertiesTextView = teamBPropertiesText
        playerB.team.balanceTextView = teamABalanceText
        playerB.team.propertiesTextView = teamAPropertiesText
        playerA.team.balanceTextView = teamABalanceText
        playerA.team.propertiesTextView = teamAPropertiesText

        teamANameText.text = playerA.team.name
        teamBNameText.text = playerC.team.name
        teamABalanceText.text = playerA.team.balance.toString()
        teamBBalanceText.text = playerC.team.balance.toString()

        val teamAOwnedPropertyNames = playerA.team.ownedProperties.map { it.fieldName }
        val teamBOwnedPropertyNames = playerC.team.ownedProperties.map { it.fieldName }

        board.forEach { f ->
            if (f is Property) {
                if (teamAOwnedPropertyNames.contains(f.fieldName)) {
                    addPropertyToTeamAfterResumingSession(f, playerA.team)
                } else if (teamBOwnedPropertyNames.contains(f.fieldName)) {
                    addPropertyToTeamAfterResumingSession(f, playerC.team)
                }
            }
        }

        playerANameText.text = playerA.name
        playerBNameText.text = playerB.name
        playerCNameText.text = playerC.name
        playerDNameText.text = playerD.name

        playerAPositionText.text = playerA.position.fieldName
        playerBPositionText.text = playerB.position.fieldName
        playerCPositionText.text = playerC.position.fieldName
        playerDPositionText.text = playerD.position.fieldName

        val currPlayer = PlayerDto.toPlayer(session.currentPlayer)

        currentPlayer = players.find { p ->
            p.name == currPlayer.name
        }!!
    }

    @SuppressLint("SetTextI18n")
    private fun addPropertyToTeamAfterResumingSession(p: Property, t: Team) {
        p.purchase(t.name)
        val newPropertyText =
            if (t.propertiesTextView!!.text.toString().isEmpty()) {
                p.fieldName
            } else {
                ", ${p.fieldName}"
            }

        t.propertiesTextView!!.text =
            t.propertiesTextView!!.text.toString() + newPropertyText
    }

    private fun initPlayers() {
        val startingBalance = intent.getIntExtra("startingBalance", 0)
        val teamA = Team(
            startingBalance,
            intent.getStringExtra("teamA")!!,
            ArrayList(),
            teamABalanceText,
            teamAPropertiesText
        )
        val teamB = Team(
            startingBalance,
            intent.getStringExtra("teamB")!!,
            ArrayList(),
            teamBBalanceText,
            teamBPropertiesText
        )
        teamANameText.text = teamA.name
        teamBNameText.text = teamB.name
        teamABalanceText.text = teamA.balance.toString()
        teamBBalanceText.text = teamB.balance.toString()

        val playerA =
            Player(intent.getStringExtra("playerA")!!, board[0], teamA, playerAPositionText)
        val playerB =
            Player(intent.getStringExtra("playerB")!!, board[0], teamA, playerBPositionText)
        val playerC =
            Player(intent.getStringExtra("playerC")!!, board[0], teamB, playerCPositionText)
        val playerD =
            Player(intent.getStringExtra("playerD")!!, board[0], teamB, playerDPositionText)

        players = arrayOf(playerA, playerB, playerC, playerD)

        playerANameText.text = playerA.name
        playerBNameText.text = playerB.name
        playerCNameText.text = playerC.name
        playerDNameText.text = playerD.name

        playerAPositionText.text = playerA.position.fieldName
        playerBPositionText.text = playerB.position.fieldName
        playerCPositionText.text = playerC.position.fieldName
        playerDPositionText.text = playerD.position.fieldName

        currentPlayer = playerA
    }

    override fun onStop() {
        mainHandler.removeCallbacks(runnable)
        sessionService.saveSession(
            Session(
                playersToPlayerDtoArray(), PlayerDto.toPlayerDto(currentPlayer)
            )
        )
        super.onStop()
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(runnable)
        sessionService.saveSession(
            Session(
                playersToPlayerDtoArray(), PlayerDto.toPlayerDto(currentPlayer)
            )
        )
        super.onDestroy()
    }

    override fun onRestart() {
        val session = sessionService.getSession()!!
        setPlayersFromSession(session)
        super.onRestart()
    }

    private fun initGame() {
        val startField = StartField("STA")

        val firstDoNothingField = DoNothingField("DON1")
        val secondDoNothingField = DoNothingField("DON2")
        val thirdDoNothingField =
            DoNothingField("DON3")    // 3 of these are corners and the other two pay 100 or 200 tax in real monopoly
        val fourthDoNothingField = DoNothingField("DON4")
        val fifthDoNothingField = DoNothingField("DON5")

        val firstChance = ChanceField("CHA1")
        val secondChance = ChanceField("CHA2")
        val thirdChance = ChanceField("CHA3")
        val fourthChance = ChanceField("CHA4")
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
            firstTrainProperty, secondTrainProperty, thirdTrainProperty, fourthTrainProperty
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

    @Suppress("UNUSED_PARAMETER")
    @RequiresApi(Build.VERSION_CODES.O)
    fun rollDice(view: View) {
        val animationDurationInMillis: Long = 1000
        val firstDice = getRandomDiceRoll()
        val secondDice = getRandomDiceRoll()
        val firstDiceImg = findViewById<View>(R.id.dice1) as ImageView
        val secondDiceImg = findViewById<View>(R.id.dice2) as ImageView

        playDiceAnimation(firstDice, firstDiceImg)
        playDiceAnimation(secondDice, secondDiceImg)

        // wait for dice animation to finish
        mainHandler.postDelayed({
            movePlayerBy(firstDice + secondDice)
            handleActionOnLandedField()

            if (!isPurchasing) { // race condition problems arise when waiting for user to choose an option in the property purchase dialog. so this is needed
                changeTurn()
            }
        }, animationDurationInMillis)
    }

    private fun playDiceAnimation(roll: Int, img: ImageView) {
        when (roll) {
            1 -> img.setBackgroundResource(R.drawable.diceroll1)
            2 -> img.setBackgroundResource(R.drawable.diceroll2)
            3 -> img.setBackgroundResource(R.drawable.diceroll3)
            4 -> img.setBackgroundResource(R.drawable.diceroll4)
            5 -> img.setBackgroundResource(R.drawable.diceroll5)
            6 -> img.setBackgroundResource(R.drawable.diceroll6)
        }

        val frameAnimation = img.background as AnimationDrawable
        frameAnimation.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun promptPropertyPurchaseAndChangeTurn(p: Property) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "Buy"
                ) { _, _ ->
                    addPropertyToTeam(p, currentPlayer.team)
                    subtractBalance(currentPlayer.team, p.cost)

                    isPurchasing = false
                    changeTurn()
                }
                setNegativeButton(
                    "Don't"
                ) { _, _ -> } // Do nothing
            }

            builder.setMessage("${currentPlayer.name}, want to purchase ${p.fieldName} for ${p.cost}?")
                .setTitle("Property Purchase")

            builder.create()
        }

        alertDialog.show()
        isPurchasing = true
    }

    private fun showCommunityChestDialog(message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(message).setTitle("Community Chest for ${currentPlayer.name}")

        builder.create().show()
    }

    private fun showChanceDialog(message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(message).setTitle("Chance for ${currentPlayer.name}")

        builder.create().show()
    }

    private fun getRandomDiceRoll(): Int {
        return (1..6).random()
    }

    private fun isPropertyASet(p: Property, otherTeam: Team): Boolean {
        val propertySet = propertySets.find { properties -> properties.contains(p) }

        propertySet!!.forEach {
            if (!it.isPurchased || it.ownedBy != otherTeam.name) {
                return false
            }
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun subtractBalance(from: Team, amount: Int) {
        from.balance -= amount
        from.balanceTextView!!.text = "${from.balance}$"

        declareBankruptcyIfTeamIsBankrupt(from)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun declareBankruptcyIfTeamIsBankrupt(t: Team) {
        if (t.balance < 0) {
            handleBankruptcy(t)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleBankruptcy(loserTeam: Team) {
        sendGameResultNotification(loserTeam, getOtherTeam(loserTeam))
        sessionService.discardSession()
        startActivity(Intent(this, MainActivity::class.java))
    }

    @SuppressLint("SetTextI18n")
    private fun addPropertyToTeam(p: Property, t: Team) {
        p.purchase(t.name)
        val newPropertyText =
            if (t.propertiesTextView!!.text.toString().isEmpty()) {
                p.fieldName
            } else {
                ", ${p.fieldName}"
            }

        t.propertiesTextView!!.text =
            t.propertiesTextView!!.text.toString() + newPropertyText
        t.ownedProperties.add(p)
    }

    private fun movePlayerBy(by: Int) {
        //TODO: show a color above position based on current position

        val goesAboveMaxIndex = board.indexOf(currentPlayer.position) + by > board.size - 1
        val aboveMaxNewIndex = board.indexOf(currentPlayer.position) + by - board.size
        val normalNewIndex = board.indexOf(currentPlayer.position) + by

        val boardIndexOfNewLocation: Int

        if (goesAboveMaxIndex) {
            boardIndexOfNewLocation = aboveMaxNewIndex
            addBalance(currentPlayer.team, 200)
        } else {
            boardIndexOfNewLocation = normalNewIndex
        }

        currentPlayer.position = board[boardIndexOfNewLocation]
        currentPlayer.positionTextView!!.text = currentPlayer.position.fieldName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleActionOnLandedField() {
        if (currentPlayer.position is Property) {
            val currentProperty = currentPlayer.position as Property

            if (currentProperty.isPurchased) {
                if (isPropertyASet(currentProperty, getOtherTeam(currentPlayer.team))) {
                    transferBalance(
                        currentPlayer.team, getOtherTeam(currentPlayer.team), currentProperty.setFee
                    )
                } else {
                    transferBalance(
                        currentPlayer.team, getOtherTeam(currentPlayer.team), currentProperty.fee
                    )
                }
            } else {
                if (currentPlayer.team.balance >= currentProperty.cost) {
                    promptPropertyPurchaseAndChangeTurn(currentProperty)
                }
            }
        } else if (currentPlayer.position is ChanceField) {
            executeChance(currentPlayer.position as ChanceField)
        } else if (currentPlayer.position is CommunityChestField) {
            executeCommunityChest(currentPlayer.position as CommunityChestField)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun executeChance(c: ChanceField) {
        var chanceExplanation = ""

        when (c.getRandomChance()) {
            Chance.GoToStart -> {
                chanceExplanation = "Go to start."
                val currentPlayerIndex = board.indexOf(currentPlayer.position)
                val spacesFromStart = 40 - currentPlayerIndex
                movePlayerBy(spacesFromStart)
            }
            Chance.ThreeSpacesForward -> {
                chanceExplanation = "Travel three spaces forwards."
                movePlayerBy(3)
                handleActionOnLandedField()
            }
            Chance.StealOneHundred -> {
                chanceExplanation = "Steal 100$ from the other team."
                transferBalance(getOtherTeam(currentPlayer.team), currentPlayer.team, 100)
            }
            Chance.ThreeSpacesBack -> {
                chanceExplanation = "Travel three spaces backwards."
                movePlayerBy(-3)
                handleActionOnLandedField()
            }
        }

        showChanceDialog(chanceExplanation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun executeCommunityChest(cc: CommunityChestField) {
        var communityChestExplanation = ""

        when (cc.getRandomCommunityChestField()) {
            CommunityChest.EarnFifty -> {
                communityChestExplanation = "You earn 50$."
                addBalance(currentPlayer.team, 50)
            }
            CommunityChest.PayFifty -> {
                communityChestExplanation = "You pay 50$ to the other team."
                transferBalance(currentPlayer.team, getOtherTeam(currentPlayer.team), 50)
            }
            CommunityChest.EarnTwoHundred -> {
                communityChestExplanation = "You earn 200$."
                addBalance(currentPlayer.team, 200)
            }
            CommunityChest.PayTwoHundred -> {
                communityChestExplanation = "You pay 200$ to the other team."
                transferBalance(currentPlayer.team, getOtherTeam(currentPlayer.team), 200)
            }
            CommunityChest.PayTwentyForEveryProperty -> {
                communityChestExplanation = "You pay 20$ for every property you own."
                transferBalance(
                    currentPlayer.team,
                    getOtherTeam(currentPlayer.team),
                    currentPlayer.team.ownedProperties.size * 20
                )
            }
        }

        showCommunityChestDialog(communityChestExplanation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendGameResultNotification(loserTeam: Team, winnerTeam: Team) {
        val notificationChannel =
            NotificationChannel(
                "Oligopoly",
                "Oligopoly",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Monopoly with teams" }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)

        val builder = NotificationCompat.Builder(this, notificationChannel.id)
            .setSmallIcon(R.drawable.notification_icon_oligopoly)
            .setContentTitle("Oligopoly Game Results")
            .setContentText("Team ${loserTeam.name} went bankrupt. Which means team ${winnerTeam.name} won!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    this@InGameActivity, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }

    private fun changeTurn() {
        val currentPlayerIndex = players.indexOf(currentPlayer)

        currentPlayer = if (currentPlayerIndex < 3) {
            players[currentPlayerIndex + 1]
        } else {
            players[0]
        }
    }

    private fun getOtherTeam(team: Team): Team {
        return players.find { p -> p.team != team }!!.team
    }
}