package com.lab7.chavez.ui.screens.profile

sealed interface ProfileState {
    data object Loading : ProfileState
    data class Display(val name: String) : ProfileState
    data object LoggedOut : ProfileState
}