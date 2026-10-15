package com.lab7.chavez.ui.screens.character

sealed interface CharactersEvent {
    data object FetchData : CharactersEvent
    data object ForceError : CharactersEvent
}