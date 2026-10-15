package com.lab7.chavez.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lab7.chavez.R
import com.lab7.chavez.ui.screens.profile.ProfileEvent
import com.lab7.chavez.ui.screens.profile.ProfileState
import com.lab7.chavez.ui.screens.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogOutClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is ProfileState.LoggedOut) {
            onLogOutClick()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator()
                }
                is ProfileState.Display -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_account_circle_24),
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(100.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentState.name,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        Button(
                            onClick = { viewModel.onEvent(ProfileEvent.OnLogOutClick) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Cerrar Sesión")
                        }
                    }
                }
                is ProfileState.LoggedOut -> {
                }
            }
        }
    }
}