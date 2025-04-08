package com.example.uthapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.uthapp.navigation.Screen
import com.example.uthapp.screens.*
import com.example.uthapp.ui.theme.UTHappTheme
import com.example.uthapp.viewmodel.AuthViewModel
import com.example.uthapp.viewmodel.TaskViewModel
import com.example.uthapp.viewmodel.AddTaskViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Khởi tạo Firebase
            FirebaseApp.initializeApp(this)
            
            setContent {
                UTHappTheme {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val taskViewModel: TaskViewModel = viewModel()

                    // Theo dõi trạng thái đăng nhập
                    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route
                    ) {
                        
                        composable(Screen.Splash.route) {
                            SplashScreen(
                                onNavigateToFirst = {
                                    navController.navigate(Screen.Onboarding.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Onboarding.route) {
                            OnboardingScreen(
                                onNavigateToHome = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Home.route) {
                            HomeScreen(
                                onSettingsClick = {
                                    navController.navigate(Screen.Settings.route)
                                },
                                onTaskClick = { taskId ->
                                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                                },
                                onAddClick = {
                                    navController.navigate(Screen.AddTask.route)
                                },
                                taskViewModel = taskViewModel
                            )
                        }

                        composable(Screen.Settings.route) {
                            SettingsScreen(
                                authViewModel = authViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                authViewModel = authViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = Screen.TaskDetail.route,
                            arguments = listOf(
                                navArgument("taskId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
                            TaskDetailScreen(
                                taskId = taskId,
                                viewModel = taskViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Screen.AddTask.route) {
                            val addTaskViewModel = remember {
                                AddTaskViewModel(taskViewModel)
                            }
                            AddTaskScreen(
                                viewModel = addTaskViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onTaskAdded = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Lỗi khi khởi tạo ứng dụng: ${e.message}")
        }
    }
}