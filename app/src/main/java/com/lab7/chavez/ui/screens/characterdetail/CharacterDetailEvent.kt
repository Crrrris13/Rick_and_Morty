package com.lab7.chavez.ui.screens.characterdetail

sealed interface CharacterDetailEvent {
    data object FetchData : CharacterDetailEvent
    data object ForceError : CharacterDetailEvent
}