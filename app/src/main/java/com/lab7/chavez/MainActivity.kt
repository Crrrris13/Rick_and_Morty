package com.lab7.chavez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.lab7.chavez.R
import com.lab7.chavez.data.CharacterDb
import com.lab7.chavez.data.LocationDb
import com.lab7.chavez.navigation.*
import com.lab7.chavez.ui.screens.CharacterDetailScreen
import com.lab7.chavez.ui.screens.CharactersScreen
import com.lab7.chavez.ui.screens.LoginScreen
import com.lab7.chavez.ui.screens.LocationDetailScreen
import com.lab7.chavez.ui.screens.LocationsScreen
import com.lab7.chavez.ui.screens.ProfileScreen
import com.lab7.chavez.ui.theme.Lab7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val characterDb = CharacterDb()
        val locationDb = LocationDb()

        setContent {
            Lab7Theme() {
                MainAppContent(characterDb, locationDb)
            }
        }
    }
}

@Composable
fun MainAppContent(characterDb: CharacterDb, locationDb: LocationDb) {
    val navController = rememberNavController()

    // pila de navegacion para saber la pantalla actual
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // mostrar bottombar en las pantallas principales
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.route?.contains("CharactersListDestination") == true ||
        it.route?.contains("LocationsListDestination") == true ||
        it.route?.contains("ProfileDestination") == true
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route?.contains("CharactersGraph") == true } == true,
                        onClick = {
                            navController.navigate(CharactersGraph) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true}
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_group),
                                contentDescription = "Characters",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route?.contains("LocationsGraph") == true } == true,
                        onClick = {
                            navController.navigate(LocationsGraph) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_public),
                                contentDescription = "Locations",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route?.contains("ProfileDestination") == true } == true,
                        onClick = {
                            navController.navigate(ProfileDestination) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_boy_24),
                                contentDescription = "Profile",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                }
            }
        }

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LoginDestination> {
                LoginScreen(
                    onStartClick = {
                        navController.navigate(CharactersGraph) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Grafo Characters
            navigation<CharactersGraph>(startDestination = CharactersListDestination) {
                composable<CharactersListDestination> {
                    CharactersScreen(
                        characters = characterDb.getAllCharacters(),
                        onCharacterClick = { id ->
                            navController.navigate(CharacterDetailDestination(id))
                        }
                    )
                }
                composable<CharacterDetailDestination> { backStackEntry ->
                    val detailRoute = backStackEntry.toRoute<CharacterDetailDestination>()
                    val character = characterDb.getCharacterById(detailRoute.characterId)
                    CharacterDetailScreen(
                        character = character,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            // Grafo Locations
            navigation<LocationsGraph>(startDestination = LocationsListDestination) {
                composable<LocationsListDestination> {
                    LocationsScreen(
                        locations = locationDb.getAllLocations(),
                        onLocationClick = { id ->
                            navController.navigate(LocationDetailDestination(id))
                        }
                    )
                }

                composable<LocationDetailDestination> { backStackEntry ->
                    val detailRoute = backStackEntry.toRoute<LocationDetailDestination>()
                    val location = locationDb.getLocationById(detailRoute.locationId)
                    LocationDetailScreen(
                        location = location,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            // Pantalla Profile
            composable<ProfileDestination> {
                ProfileScreen(
                    onLogOutClick = {
                        navController.navigate(LoginDestination) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainAppPreview() {
    Lab7Theme() {
        MainAppContent(characterDb = CharacterDb(), locationDb = LocationDb())
    }
}