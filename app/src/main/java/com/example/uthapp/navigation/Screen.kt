package com.example.uthapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object First : Screen("first")
    object Second : Screen("second")
    object Third : Screen("third")
    object Home : Screen("home")
} 