package com.lab7.chavez.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lab7.chavez.data.Character
import com.lab7.chavez.data.CharacterDb
import com.lab7.chavez.navigation.CharacterDetailDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(CharacterDetailUiState())
    val state: StateFlow<CharacterDetailUiState> = _state.asStateFlow()

    private val characterDb = CharacterDb()

    private val characterId = savedStateHandle.toRoute<CharacterDetailDestination>().characterId

    init {
        fetchCharacterDetail()
    }

    fun fetchCharacterDetail() {
        _state.value = _state.value.copy(isLoading = true, hasError = false)
        viewModelScope.launch {
            delay(2000)
            _state.value = _state.value.copy(
                isLoading = false,
                data = characterDb.getCharacterById(characterId)
            )
        }
    }

    fun triggerError() {
        _state.value = _state.value.copy(isLoading = false, hasError = true)
    }
}
