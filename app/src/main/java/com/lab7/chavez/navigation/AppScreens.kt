package com.lab7.chavez.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginDestination

@Serializable
object CharactersGraph

@Serializable
object LocationsGraph

@Serializable
object CharactersListDestination

@Serializable
data class CharacterDetailDestination(val characterId: Int)

@Serializable
object LocationsListDestination

@Serializable
data class LocationDetailDestination(val locationId: Int)

@Serializable
object ProfileDestination