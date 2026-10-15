package com.lab7.chavez.ui.screens.login

sealed interface LoginEvent {
    data object OnLoginClick : LoginEvent
    data class OnNameChange(val name: String) : LoginEvent
}

