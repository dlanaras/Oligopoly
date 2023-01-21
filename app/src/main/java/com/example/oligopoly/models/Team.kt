package com.example.oligopoly.models

import android.widget.TextView


class Team(var balance: Int, val name: String, val ownedProperties: ArrayList<Property>, var balanceTextView: TextView?, var propertiesTextView: TextView?) {
}