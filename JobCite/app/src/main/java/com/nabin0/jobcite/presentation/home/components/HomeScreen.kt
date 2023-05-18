package com.nabin0.jobcite.presentation.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nabin0.jobcite.R
import com.nabin0.jobcite.presentation.graphs.HomeNavGraph
import com.nabin0.jobcite.presentation.screens.BottomBarScreens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    toggleTheme: () -> Unit,
    rootNavHost: NavHostController
) {
    Scaffold(bottomBar = {
        BottomBar(navHostController = navHostController)
    }) { paddingValues ->
        HomeNavGraph(
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
            navHostController = navHostController,
            toggleTheme = toggleTheme, rootNavHost = rootNavHost
        )
    }
}


@Composable
fun BottomBar(navHostController: NavHostController) {
    val screens = listOf(
        BottomBarScreens.Home,
        BottomBarScreens.Resources,
        BottomBarScreens.Discuss,
        BottomBarScreens.Settings
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any {
        it.route == currentDestination?.route
    }

    if (bottomBarDestination) {
        BottomNavigation(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navHostController = navHostController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreens,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    val itemColor = if (isSelected){
        colorResource(id = R.color.primary2)
    }else{
        colorResource(id = R.color.primary2).copy(0.5f)
    }
    BottomNavigationItem(label = {
        Text(
            text = screen.title, color = itemColor
        )
    },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon",
            )
        },
        selected = isSelected,
        unselectedContentColor = itemColor,
        selectedContentColor = itemColor,
        onClick = {
            navHostController.navigate(screen.route) {
                popUpTo(navHostController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        })
}

