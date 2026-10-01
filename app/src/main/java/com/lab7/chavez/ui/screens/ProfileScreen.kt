package com.lab7.chavez.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import com.lab7.chavez.R

@Composable
fun ProfileScreen(
    onLogOutClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_account_circle_24),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            DetailRow(label = "Nombre:", value = "Cristopher Javier Chávez Toc")
            Spacer(modifier = Modifier.height(16.dp))
            DetailRow(label = "Carné:", value = "25199")

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedButton(onClick = onLogOutClick) {
                Text("Cerrar sesión")
            }
        }
    }

}