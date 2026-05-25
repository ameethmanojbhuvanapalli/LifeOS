package com.lifeos.core.navigation

sealed class NavDestinations(val route: String) {
    data object Dashboard : NavDestinations("dashboard")
    data object TodoList : NavDestinations("todo")
    data object FinanceList : NavDestinations("finance")
    data object TrackerList : NavDestinations("tracker")
    data object Settings : NavDestinations("settings")
}
