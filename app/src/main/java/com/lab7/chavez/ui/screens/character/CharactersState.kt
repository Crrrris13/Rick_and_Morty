package com.lab7.chavez.ui.screens.character

import com.lab7.chavez.data.Character

sealed interface CharactersState {
    data object Loading : CharactersState
    data class Success(val data: List<Character>) : CharactersState
    data class Error(val message: String) : CharactersState
}