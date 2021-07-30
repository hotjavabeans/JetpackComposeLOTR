package com.example.jetpackcomposelotr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposelotr.ui.characterdetail.CharacterDetailScreen
import com.example.jetpackcomposelotr.ui.characterlist.CharacterListScreen
import com.example.jetpackcomposelotr.ui.theme.JetpackComposeLOTRTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeLOTRTheme {
                LOTRApp()
            }
        }
    }
}

@Composable
fun LOTRApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CharacterListScreen.route) {
        composable(Screen.CharacterListScreen.route) {
            CharacterListScreen(navController = navController)
        }
        composable(
            Screen.CharacterDetailScreen.route + "/{id}",
            arguments = listOf(
                /*navArgument("characterName") {
                    type = NavType.StringType
                },
                navArgument("race") {
                    type = NavType.StringType
                },*/
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) {
            /*val name = remember {
                it.arguments?.getString("characterName")
            }
            val race = remember {
                it.arguments?.getString("race")
            }*/
            val id = remember {
                it.arguments?.getString("id")
            }
            CharacterDetailScreen(
                navController = navController,
                characterId = id ?: "empty id"
            )
            /*characterName = characterName?.lowercase(Locale.ROOT) ?: "name: empty",
            characterRace = race ?: "race: empty"*/
        }
    }
}
