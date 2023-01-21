package com.example.oligopoly.models

import com.example.oligopoly.interfaces.Field
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@kotlinx.serialization.Serializable
class PlayerDto(
    val name: String,
    @Polymorphic val position: Field,
    val team: TeamDto
) {
    companion object {
        fun toPlayer(pDto: PlayerDto): Player {
            return Player(pDto.name, pDto.position, TeamDto.toTeam(pDto.team)!!, null, null)
        }

        fun toPlayerDto(p: Player): PlayerDto {
            return PlayerDto(p.name, p.position, TeamDto.toTeamDto(p.team)!!)
        }
    }
}

val fieldModule = SerializersModule {
    fun PolymorphicModuleBuilder<Field>.registerProjectSubClasses() {
        subclass(Property::class, Property.serializer())
        subclass(CommunityChestField::class, CommunityChestField.serializer())
        subclass(ChanceField::class, ChanceField.serializer())
        subclass(DoNothingField::class, DoNothingField.serializer())
        subclass(StartField::class, StartField.serializer())
    }

    polymorphic(Field::class) { registerProjectSubClasses() }
}



val format = Json { serializersModule = fieldModule }