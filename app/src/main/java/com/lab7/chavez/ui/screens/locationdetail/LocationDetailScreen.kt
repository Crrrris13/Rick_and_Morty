package com.lab7.chavez.ui.screens.locationdetail

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
import com.lab7.chavez.ui.common.Error
import com.lab7.chavez.ui.common.Loading
import com.lab7.chavez.ui.screens.characterdetail.DetailRow
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
                        Icon(painter = painterResource(id = R.drawable.outline_arrow_left_alt_24), contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (val currentState = state) {
                is LocationDetailState.Error -> Error(
                    errorMessage = currentState.message,
                    onRetryClick = { viewModel.onEvent(LocationDetailEvent.FetchData) }
                )
                is LocationDetailState.Loading -> Loading(
                    onLoadingClick = { viewModel.onEvent(LocationDetailEvent.ForceError) }
                )
                is LocationDetailState.Success -> {
                    val location = currentState.data
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