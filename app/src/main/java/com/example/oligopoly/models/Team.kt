package com.example.oligopoly.models

import android.widget.TextView


class Team(var balance: Int, val name: String, val ownedProperties: ArrayList<Property>, val balanceTextView: TextView?, val propertiesTextView: TextView?) {
}