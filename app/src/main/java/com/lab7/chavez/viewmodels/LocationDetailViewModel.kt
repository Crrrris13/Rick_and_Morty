package com.lab7.chavez.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lab7.chavez.data.Location
import com.lab7.chavez.data.LocationDb
import com.lab7.chavez.navigation.LocationDetailDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(LocationDetailUiState())
    val state: StateFlow<LocationDetailUiState> = _state.asStateFlow()

    private val locationDb = LocationDb()

    private val locationId = savedStateHandle.toRoute<LocationDetailDestination>().locationId

    init {
        fetchLocationDetail()
    }

    fun fetchLocationDetail() {
        _state.value = _state.value.copy(isLoading = true, hasError = false)
        viewModelScope.launch {
            delay(2000)
            _state.value = _state.value.copy(
                isLoading = false,
                data = locationDb.getLocationById(locationId)
            )
        }
    }

    fun triggerError() {
        _state.value = _state.value.copy(isLoading = false, hasError = true)
    }
}
