package com.lifeos.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Dashboard : BottomNavItem(route = "dashboard", label = "Home", icon = Icons.Default.Home)
    data object Todos : BottomNavItem(route = "todo", label = "Todos", icon = Icons.Default.List)
}

@Composable
fun LifeOSBottomBar(
    selectedRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(BottomNavItem.Dashboard, BottomNavItem.Todos)

    NavigationBar {
        items.forEach { item ->
            val selected = selectedRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
