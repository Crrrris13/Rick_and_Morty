package com.lab7.chavez.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.lab7.chavez.data.Character
import com.lab7.chavez.ui.screens.character.CharactersEvent
import com.lab7.chavez.ui.screens.character.CharactersState
import com.lab7.chavez.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            when (val currentState = state) {
                is CharactersState.Error -> {
                    Error(
                        errorMessage = currentState.message,
                        onRetryClick = { viewModel.onEvent(CharactersEvent.FetchData) }
                    )
                }
                is CharactersState.Loading -> {
                    Loading(
                        onLoadingClick = { viewModel.onEvent(CharactersEvent.ForceError) }
                    )
                }
                is CharactersState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(currentState.data) { character ->
                            CharacterItem(
                                character = character,
                                onClick = { onCharacterClick(character.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = character.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${character.species} - ${character.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
