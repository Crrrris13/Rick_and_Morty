package com.lab7.chavez.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab7.chavez.data.Character
import com.lab7.chavez.data.CharacterDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel : ViewModel() {
    private val  _state = MutableStateFlow(CharactersUiState())
    val state: StateFlow<CharactersUiState> = _state.asStateFlow()

    private val characterDb = CharacterDb()

    init {
        fetchCharacters()
    }

    fun  fetchCharacters() {
        _state.value = _state.value.copy(isLoading = true, hasError = false)

        viewModelScope.launch {
            delay(4000)
            _state.value = _state.value.copy(
                isLoading = false,
                data = characterDb.getAllCharacters()
            )
        }
    }

    fun triggerError() {
        _state.value = _state.value.copy(isLoading = false, hasError = true)
    }
}