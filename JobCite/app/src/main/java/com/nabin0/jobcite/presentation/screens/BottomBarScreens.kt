package com.nabin0.jobcite.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreens(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreens(
        route = "HOME",
        title = "HOME",
        icon = Icons.Outlined.Home
    )
    object Resources: BottomBarScreens(
        route = "RESOURCES",
        title = "RESOURCES",
        icon = Icons.Outlined.Book
    )
    object Discuss: BottomBarScreens(
        route = "DISCUSS",
        title = "DISCUSS",
        icon = Icons.Outlined.Message
    )
    object Settings: BottomBarScreens(
        route = "SETTINGS",
        title = "SETTINGS",
        icon = Icons.Outlined.Settings
    )
}