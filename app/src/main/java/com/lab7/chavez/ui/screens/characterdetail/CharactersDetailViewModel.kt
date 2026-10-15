package com.lab7.chavez.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lab7.chavez.data.CharacterDb
import com.lab7.chavez.navigation.CharacterDetailDestination
import com.lab7.chavez.ui.screens.characterdetail.CharacterDetailEvent
import com.lab7.chavez.ui.screens.characterdetail.CharacterDetailState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    private val characterDb = CharacterDb()
    private val characterId = savedStateHandle.toRoute<CharacterDetailDestination>().characterId
    private var fetchJob: Job? = null

    init {
        onEvent(CharacterDetailEvent.FetchData)
    }

    fun onEvent(event: CharacterDetailEvent) {
        when (event) {
            is CharacterDetailEvent.FetchData -> fetchCharacterDetail()
            is CharacterDetailEvent.ForceError -> triggerError()
        }
    }

    private fun fetchCharacterDetail() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _state.value = CharacterDetailState.Loading
            delay(2000L)
            try {
                val character = characterDb.getCharacterById(characterId)
                if (character != null) {
                    _state.value = CharacterDetailState.Success(character)
                } else {
                    _state.value = CharacterDetailState.Error("Personaje no encontrado.")
                }
            } catch (e: Exception) {
                _state.value = CharacterDetailState.Error("Error al cargar el perfil.")
            }
        }
    }

    private fun triggerError() {
        fetchJob?.cancel()
        _state.value = CharacterDetailState.Error("Error de carga.")
    }
}