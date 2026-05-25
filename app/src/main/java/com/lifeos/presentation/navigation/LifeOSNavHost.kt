package com.lifeos.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifeos.core.navigation.NavDestinations
import com.lifeos.presentation.screens.DashboardScreen
import com.lifeos.presentation.todo.AddEditTodoRoute
import com.lifeos.presentation.todo.TodoListRoute
import androidx.compose.foundation.layout.padding

@Composable
fun LifeOSNavHost() {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // hide on add/edit screen
            if (currentRoute != "todo/edit?todoId={todoId}" && currentRoute != "todo/add") {
                LifeOSBottomBar(currentRoute = currentRoute) { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavDestinations.Dashboard.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(NavDestinations.Dashboard.route) {
                DashboardScreen(onOpenTodos = { navController.navigate(NavDestinations.TodoList.route) })
            }
            composable(NavDestinations.TodoList.route) {
                TodoListRoute(
                    onAdd = { navController.navigate("todo/add") },
                    onEdit = { id -> navController.navigate("todo/edit?todoId=$id") }
                )
            }
            composable("todo/add") {
                AddEditTodoRoute(onBack = { navController.popBackStack() })
            }
            composable("todo/edit?todoId={todoId}") {
                AddEditTodoRoute(onBack = { navController.popBackStack() })
            }
        }
    }
}
