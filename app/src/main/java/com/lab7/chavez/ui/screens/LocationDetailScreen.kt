package com.lab7.chavez.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lab7.chavez.R
import com.lab7.chavez.viewmodels.LocationDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    onBackClick: () -> Unit,
    viewModel: LocationDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_left_alt_24),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when {
                state.hasError -> {
                    Error(
                        errorMessage = "Error al obtener el perfil de la ubicación.",
                        onRetryClick = { viewModel.fetchLocationDetail() }
                    )
                }

                state.isLoading -> {
                    Loading(
                        onLoadingClick = { viewModel.triggerError() }
                    )
                }

                else -> {
                    state.data?.let { location ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier.height(48.dp)
                            )

                            Text(
                                text = location.name,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(
                                modifier = Modifier.height(48.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp)
                            ) {
                                DetailRow(label = "ID:", value = location.id.toString())
                                Spacer(modifier = Modifier.height(16.dp))
                                DetailRow(label = "Type:", value = location.type)
                                Spacer(modifier = Modifier.height(16.dp))
                                DetailRow(label = "Dimensions:", value = location.dimension)
                            }
                        }
                    }
                }
            }
        }
    }
}