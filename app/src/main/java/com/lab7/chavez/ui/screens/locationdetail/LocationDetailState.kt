package com.lab7.chavez.ui.screens.locationdetail

import com.lab7.chavez.data.Location

sealed interface LocationDetailState {
    data object Loading : LocationDetailState
    data class Success(val data: Location) : LocationDetailState
    data class Error(val message: String) : LocationDetailState
}