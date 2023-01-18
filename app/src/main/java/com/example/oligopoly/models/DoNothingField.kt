package com.example.oligopoly.models

import com.example.oligopoly.enums.Color
import com.example.oligopoly.interfaces.Field

class DoNothingField(override val fieldName: String, override val color: Color = Color.None) :
    Field {
}