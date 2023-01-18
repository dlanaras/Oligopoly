package com.example.oligopoly.models

import com.example.oligopoly.enums.Color
import com.example.oligopoly.interfaces.Field

class Property(
    override val fieldName: String,
    override val color: Color,
    val isPurchased: Boolean = false,
    val ownedBy: Team,
    val cost: Int,
    val fee: Int,
    val setFee: Int
) : Field {
}