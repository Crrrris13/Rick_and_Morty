package com.lab7.chavez.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab7.chavez.data.Location
import com.lab7.chavez.data.LocationDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel : ViewModel() {
    private val _state = MutableStateFlow(LocationsUiState())
    val state: StateFlow<LocationsUiState> = _state.asStateFlow()

    private val locationDb = LocationDb()

    init {
        fetchLocations()
    }

    fun fetchLocations() {
        _state.value = _state.value.copy(isLoading = true, hasError = false)
        viewModelScope.launch {
            delay(4000)
            _state.value = _state.value.copy(
                isLoading = false,
                data = locationDb.getAllLocations()
            )
        }
    }

    fun triggerError() {
        _state.value = _state.value.copy(isLoading = false, hasError = true)
    }
}