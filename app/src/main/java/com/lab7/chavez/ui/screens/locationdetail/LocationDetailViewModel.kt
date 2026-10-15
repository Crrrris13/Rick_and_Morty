package com.lab7.chavez.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lab7.chavez.data.LocationDb
import com.lab7.chavez.navigation.LocationDetailDestination
import com.lab7.chavez.ui.screens.locationdetail.LocationDetailEvent
import com.lab7.chavez.ui.screens.locationdetail.LocationDetailState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<LocationDetailState>(LocationDetailState.Loading)
    val state: StateFlow<LocationDetailState> = _state.asStateFlow()

    private val locationDb = LocationDb()
    private val locationId = savedStateHandle.toRoute<LocationDetailDestination>().locationId
    private var fetchJob: Job? = null

    init {
        onEvent(LocationDetailEvent.FetchData)
    }

    fun onEvent(event: LocationDetailEvent) {
        when (event) {
            is LocationDetailEvent.FetchData -> fetchLocationDetail()
            is LocationDetailEvent.ForceError -> triggerError()
        }
    }

    private fun fetchLocationDetail() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _state.value = LocationDetailState.Loading
            delay(2000L)
            try {
                val location = locationDb.getLocationById(locationId)
                if (location != null) {
                    _state.value = LocationDetailState.Success(location)
                } else {
                    _state.value = LocationDetailState.Error("Ubicación no encontrada.")
                }
            } catch (e: Exception) {
                _state.value = LocationDetailState.Error("Error al cargar la ubicación.")
            }
        }
    }

    private fun triggerError() {
        fetchJob?.cancel()
        _state.value = LocationDetailState.Error("Error de carga.")
    }
}