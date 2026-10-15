package com.lab7.chavez.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lab7.chavez.R
import com.lab7.chavez.ui.screens.login.LoginEvent
import com.lab7.chavez.ui.screens.login.LoginState
import com.lab7.chavez.ui.screens.login.LoginViewModel

@Composable
fun LoginScreen(
    onStartClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            onStartClick()
        }
    }

    val currentName = when (val s = state) {
        is LoginState.Idle -> s.name
        is LoginState.Loading -> s.name
        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.rick_and_morty),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = currentName,
                onValueChange = { viewModel.onEvent(LoginEvent.OnNameChange(it)) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                enabled = state !is LoginState.Loading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEvent(LoginEvent.OnLoginClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = currentName.isNotBlank() && state !is LoginState.Loading
            ) {
                if (state is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}