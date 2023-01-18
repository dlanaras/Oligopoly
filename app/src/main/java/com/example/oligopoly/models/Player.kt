package com.example.oligopoly.models

import android.widget.TextView
import com.example.oligopoly.interfaces.Field

class Player(val name: String, var position: Field, val team: Team, val positionTextView: TextView?) {
}