package com.lab7.chavez.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lab7.chavez.data.Location
import com.lab7.chavez.ui.screens.location.LocationsEvent
import com.lab7.chavez.ui.screens.location.LocationsState
import com.lab7.chavez.viewmodels.LocationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (val currentState = state) {
                is LocationsState.Error -> Error(
                    errorMessage = currentState.message,
                    onRetryClick = { viewModel.onEvent(LocationsEvent.FetchData) }
                )
                is LocationsState.Loading -> Loading(
                    onLoadingClick = { viewModel.onEvent(LocationsEvent.ForceError) }
                )
                is LocationsState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(currentState.data) { location ->
                            LocationItem(
                                location = location,
                                onClick = { onLocationClick(location.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItem(location: Location, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = location.name,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = location.type,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}