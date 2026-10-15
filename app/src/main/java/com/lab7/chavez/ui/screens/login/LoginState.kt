package com.lab7.chavez.ui.screens.login

sealed interface LoginState {
    data class Idle(val name: String = "") : LoginState
    data class Loading(val name: String = "") : LoginState
    data object Success : LoginState
}