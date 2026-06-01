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
import androidx.compose.ui.res.stringResource
import com.lifeos.R

sealed class BottomNavItem(val route: String, val labelRes: Int, val icon: ImageVector) {
    data object Dashboard : BottomNavItem(route = "dashboard", labelRes = R.string.nav_home, icon = Icons.Default.Home)
    data object Todos : BottomNavItem(route = "todo", labelRes = R.string.nav_todos, icon = Icons.Default.List)
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
            val label = stringResource(item.labelRes)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
