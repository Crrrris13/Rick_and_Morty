package com.lab7.chavez.ui.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lab7.chavez.data.UserPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val userPreferences = UserPreferences(application)
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

            _state.value = LoginState.Success
        }
    }
}