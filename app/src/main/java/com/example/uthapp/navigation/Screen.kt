package com.example.uthapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
    object AddTask : Screen("add_task")
    object TaskDetail : Screen("task/{taskId}") {
        fun createRoute(taskId: Int) = "task/$taskId"
    }
} 