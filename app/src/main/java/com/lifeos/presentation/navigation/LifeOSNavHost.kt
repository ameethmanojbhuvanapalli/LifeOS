package com.lifeos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lifeos.core.navigation.NavDestinations
import com.lifeos.presentation.screens.DashboardScreen
import com.lifeos.presentation.screens.TodoListScreen

@Composable
fun LifeOSNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavDestinations.Dashboard.route) {
        composable(NavDestinations.Dashboard.route) {
            DashboardScreen(
                onOpenTodos = { navController.navigate(NavDestinations.TodoList.route) }
            )
        }
        composable(NavDestinations.TodoList.route) {
            TodoListScreen()
        }
    }
}
