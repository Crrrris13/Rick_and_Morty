package com.lab7.chavez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lab7.chavez.ui.theme.Lab7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab7Theme() {
                MainAppContent() // Llamamos al composable principal aquí
            }
        }
    }
}

@Composable
fun MainAppContent() {
    val navController = rememberNavController()
    val characterDb = CharacterDb()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        composable<LoginDestination> {
            LoginScreen(
                onStartClick = {
                    navController.navigate(CharactersDestination) {
                        popUpTo<LoginDestination> { inclusive = true }
                    }
                }
            )
        }
        composable<CharactersDestination> {
            CharactersScreen(
                characters = characterDb.getAllCharacters(),
                onCharacterClick = { characterId ->
                    navController.navigate(CharacterDetailDestination(characterId = characterId))
                }
            )
        }
        composable<CharacterDetailDestination> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<CharacterDetailDestination>()
            val character = characterDb.getCharacterById(detailRoute.characterId)

            CharacterDetailScreen(
                character = character,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainAppPreview() {
    Lab7Theme() {
        MainAppContent()
    }
}