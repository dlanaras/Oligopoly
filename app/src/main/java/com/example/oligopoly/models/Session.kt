package com.example.oligopoly.models

@kotlinx.serialization.Serializable
class Session(val players: Array<PlayerDto>, val currentPlayer: PlayerDto) {
}