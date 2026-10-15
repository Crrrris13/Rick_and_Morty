package com.lab7.chavez.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab7.chavez.data.LocationDb
import com.lab7.chavez.ui.screens.location.LocationsEvent
import com.lab7.chavez.ui.screens.location.LocationsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationsViewModel : ViewModel() {
    private val _state = MutableStateFlow<LocationsState>(LocationsState.Loading)
    val state: StateFlow<LocationsState> = _state.asStateFlow()

    private val locationDb = LocationDb()
    private var fetchJob: Job? = null

    init {
        onEvent(LocationsEvent.FetchData)
    }

    fun onEvent(event: LocationsEvent) {
        when (event) {
            is LocationsEvent.FetchData -> fetchLocations()
            is LocationsEvent.ForceError -> triggerError()
        }
    }

    private fun fetchLocations() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _state.value = LocationsState.Loading
            delay(4000L)
            try {
                _state.value = LocationsState.Success(locationDb.getAllLocations())
            } catch (e: Exception) {
                _state.value = LocationsState.Error("Error al obtener listado de ubicaciones.")
            }
        }
    }

    private fun triggerError() {
        fetchJob?.cancel()
        _state.value = LocationsState.Error("Error de carga.")
    }
}