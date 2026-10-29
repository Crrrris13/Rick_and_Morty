package com.lab7.chavez.ui.screens.character

sealed interface CharactersEvent {
    data object FetchData : CharactersEvent
    data object ForceError : CharactersEvent
    data class ToggleFavorite(val id: Int, val currentStatus: Boolean) : CharactersEvent
    data object ToggleFilter : CharactersEvent
}