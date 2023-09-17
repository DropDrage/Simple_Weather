package com.dropdrage.simpleweather

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal fun NavGraphBuilder.composable(
    route: Route,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(route = route.route, content = content)
}

internal fun NavController.navigate(route: Route) {
    navigate(route = route.route)
}
