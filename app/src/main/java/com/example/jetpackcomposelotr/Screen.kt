package com.example.jetpackcomposelotr

sealed class Screen(val route: String) {
    object CharacterListScreen : Screen(route = "character_list_screen")
    object CharacterDetailScreen : Screen(route = "character_detail_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/entry.$arg")
            }
        }
    }
}
