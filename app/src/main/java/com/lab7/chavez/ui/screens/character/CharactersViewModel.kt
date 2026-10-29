package com.lab7.chavez.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lab7.chavez.data.AppDatabase
import com.lab7.chavez.data.toModel
import com.lab7.chavez.ui.screens.character.CharactersEvent
import com.lab7.chavez.ui.screens.character.CharactersState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharactersViewModel(application: Application) : AndroidViewModel(application) {

    private val characterDao = AppDatabase.getDatabase(application).characterDao()
    private val isFilterActive = MutableStateFlow(false)

    val state: StateFlow<CharactersState> = combine(
        characterDao.getAllCharacters(),
        isFilterActive
    ) { entities, filterActive ->
        val models = entities.map { it.toModel() }
        val filteredList = if (filterActive) models.filter { it.isFavorite } else models

        CharactersState.Success(data = filteredList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CharactersState.Loading
    )

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    characterDao.updateFavoriteStatus(event.id, !event.currentStatus)
                }
            }
            is CharactersEvent.ToggleFilter -> {
                isFilterActive.value = !isFilterActive.value
            }
            is CharactersEvent.FetchData -> {  }
            is CharactersEvent.ForceError -> {  }
        }
    }
}