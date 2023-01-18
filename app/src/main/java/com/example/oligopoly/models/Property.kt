package com.example.oligopoly.models

import com.example.oligopoly.enums.Color
import com.example.oligopoly.interfaces.Field

class Property(
    override val fieldName: String,
    override val color: Color,
    var isPurchased: Boolean = false,
    var ownedBy: Team? = null,
    val cost: Int,
    val fee: Int,
    val setFee: Int
) : Field {
    public fun purchase(team: Team) {
        isPurchased = true
        ownedBy = team
    }
}