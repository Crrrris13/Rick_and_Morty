package com.lab7.chavez.ui.screens.locationdetail

sealed interface LocationDetailEvent {
    data object FetchData : LocationDetailEvent
    data object ForceError : LocationDetailEvent
}