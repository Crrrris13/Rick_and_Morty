package com.lab7.chavez.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lab7.chavez.data.UserPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val userPreferences = UserPreferences(application)
    private var logoutJob: Job? = null

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userPreferences.userName.collect { savedName ->
                if (savedName != null) {
                    _state.value = ProfileState.Display(name = savedName)
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnLogOutClick -> performLogout()
        }
    }

    private fun performLogout() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            _state.value = ProfileState.Loading

            userPreferences.clearName()

            _state.value = ProfileState.LoggedOut
        }
    }
}