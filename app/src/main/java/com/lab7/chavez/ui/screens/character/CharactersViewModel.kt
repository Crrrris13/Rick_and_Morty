package com.lab7.chavez.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab7.chavez.data.CharacterDb
import com.lab7.chavez.ui.screens.character.CharactersEvent
import com.lab7.chavez.ui.screens.character.CharactersState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {

    private val _state = MutableStateFlow<CharactersState>(CharactersState.Loading)
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    private val characterDb = CharacterDb()

    private var fetchJob: Job? = null

    init {
        onEvent(CharactersEvent.FetchData)
    }

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.FetchData -> fetchCharacters()
            is CharactersEvent.ForceError -> triggerError()
        }
    }

    private fun fetchCharacters() {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            _state.value = CharactersState.Loading

            delay(4000L)

            try {
                _state.value = CharactersState.Success(characterDb.getAllCharacters())
            } catch (e: Exception) {
                _state.value = CharactersState.Error("Error al obtener el listado de personajes.")
            }
        }
    }

    private fun triggerError() {
        fetchJob?.cancel()
        _state.value = CharactersState.Error("Error de carga.")
    }
}