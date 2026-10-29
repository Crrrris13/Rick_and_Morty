package com.lab7.chavez.ui.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import com.lab7.chavez.data.AppDatabase
import com.lab7.chavez.data.CharacterDb
import com.lab7.chavez.data.LocationDb
import com.lab7.chavez.data.UserPreferences
import com.lab7.chavez.data.toEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val userPreferences = UserPreferences(application)

    private val database = AppDatabase.getDatabase(application)
    private var loginJob: Job? = null

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnNameChange -> {
                _state.value = LoginState.Idle(name = event.name)
            }
            is LoginEvent.OnLoginClick -> performLogin()
        }
    }

    private fun performLogin() {
        val currentName = when (val currentState = _state.value) {
            is LoginState.Idle -> currentState.name
            is LoginState.Loading -> currentState.name
            else -> ""
        }

        if (currentName.isBlank()) return

        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _state.value = LoginState.Loading(name = currentName)
            userPreferences.saveName(currentName)

            val characterDao = database.characterDao()
            val locationDao = database.locationDao()

            val existingCharacters = characterDao.getAllCharacters().first()

            if (existingCharacters.isEmpty()) {
                val charsToInsert = CharacterDb().getAllCharacters().map { it.toEntity() }
                val locsToInsert = LocationDb().getAllLocations().map { it.toEntity() }

                characterDao.insertAll(charsToInsert)
                locationDao.insertAll(locsToInsert)

                delay(4000L)
            }

            _state.value = LoginState.Success
        }
    }
}