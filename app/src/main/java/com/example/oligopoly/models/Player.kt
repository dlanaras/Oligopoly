package com.example.oligopoly.models

import android.view.View
import android.widget.TextView
import com.example.oligopoly.interfaces.Field

class Player(var name: String, var position: Field, var team: Team, var positionTextView: TextView?, var positionColorView: View?, var nameTextView: TextView?) {
}