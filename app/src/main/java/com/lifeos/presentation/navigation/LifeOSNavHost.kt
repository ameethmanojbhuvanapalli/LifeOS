package com.lifeos.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifeos.core.navigation.NavDestinations
import com.lifeos.presentation.screens.DashboardScreen
import com.lifeos.presentation.todo.AddEditTodoRoute
import com.lifeos.presentation.todo.TodoListRoute

@Composable
fun LifeOSNavHost() {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route

    val bottomBarVisible = currentRoute != "todo/add" && currentRoute != "todo/edit?todoId={todoId}"

    Scaffold(
        bottomBar = {
            if (bottomBarVisible) {
                LifeOSBottomBar(
                    selectedRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
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
