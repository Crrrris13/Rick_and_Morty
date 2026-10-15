package com.lab7.chavez.ui.screens.location

sealed interface LocationsEvent {
    data object FetchData : LocationsEvent
    data object ForceError : LocationsEvent
}