package com.example.oligopoly.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class TeamDto(val balance: Int, val name: String, val ownedProperties: ArrayList<PropertyDto>) {
    companion object {
        fun toTeam(tDto: TeamDto?): Team? {
            if(tDto == null) {
                return null
            }

            val properties = ArrayList<Property>()

            tDto.ownedProperties.forEach() {
                properties.add(PropertyDto.toProperty(it))
            }

            return Team(tDto.balance, tDto.name, properties, null, null)
        }

        fun toTeamDto(t: Team?): TeamDto? {
            if(t == null) {
                return null
            }

            val properties = ArrayList<PropertyDto>()

            t.ownedProperties.forEach() {
                properties.add(PropertyDto.toPropertyDto(it))
            }

            return TeamDto(t.balance, t.name, properties)
        }
    }
}