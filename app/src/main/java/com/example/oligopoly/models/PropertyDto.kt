package com.example.oligopoly.models

import com.example.oligopoly.enums.Color
import com.example.oligopoly.interfaces.Field
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class PropertyDto(
    override val fieldName: String, override val color: Color, val isPurchased: Boolean = false,
    val ownedBy: String? = null,
    val cost: Int,
    val fee: Int,
    val setFee: Int
) : Field {
    companion object {
        fun toProperty(pDto: PropertyDto) : Property {
            return Property(pDto.fieldName, pDto.color, pDto.isPurchased, pDto.ownedBy, pDto.cost, pDto.fee, pDto.setFee)
        }

        fun toPropertyDto(p: Property): PropertyDto {
            return PropertyDto(p.fieldName, p.color, p.isPurchased, p.ownedBy, p.cost, p.fee, p.setFee)
        }
    }
}