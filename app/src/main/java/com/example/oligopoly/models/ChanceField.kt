package com.example.oligopoly.models

import com.example.oligopoly.enums.Chance
import com.example.oligopoly.enums.Color
import com.example.oligopoly.interfaces.Field

class ChanceField(override val fieldName: String, override val color: Color = Color.None) : Field {
    public fun getRandomChance(): Chance {
        return Chance.values().random()
    }
}