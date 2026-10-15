package com.lab7.chavez.ui.screens.profile

sealed interface ProfileEvent {
    data object OnLogOutClick : ProfileEvent
}