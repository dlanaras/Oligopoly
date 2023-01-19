package com.example.oligopoly.models

import com.example.oligopoly.enums.Color
import com.example.oligopoly.interfaces.Field
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class Property(
    override val fieldName: String,
    override val color: Color,
    var isPurchased: Boolean = false,
    var ownedBy: String? = null,
    val cost: Int,
    val fee: Int,
    val setFee: Int
) : Field {
    public fun purchase(teamName: String) {
        isPurchased = true
        ownedBy = teamName
    }
}