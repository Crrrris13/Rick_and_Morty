package com.lab7.chavez.ui.screens.characterdetail

import com.lab7.chavez.data.Character

sealed interface CharacterDetailState {
    data object Loading : CharacterDetailState
    data class Success(val data: Character) : CharacterDetailState
    data class Error(val message: String) : CharacterDetailState
}