package com.lab7.chavez.ui.screens.location

import com.lab7.chavez.data.Location

sealed interface LocationsState {
    data object Loading : LocationsState
    data class Success(val data: List<Location>) : LocationsState
    data class Error(val message: String) : LocationsState
}