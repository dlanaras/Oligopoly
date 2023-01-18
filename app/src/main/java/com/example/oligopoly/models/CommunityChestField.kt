package com.example.oligopoly.models

import com.example.oligopoly.enums.Color
import com.example.oligopoly.enums.CommunityChest
import com.example.oligopoly.interfaces.Field

class CommunityChestField(override val fieldName: String, override val color: Color = Color.None) :
    Field {
    public fun getRandomCommunityChestField(): CommunityChest {
        return CommunityChest.values().random()
    }
}