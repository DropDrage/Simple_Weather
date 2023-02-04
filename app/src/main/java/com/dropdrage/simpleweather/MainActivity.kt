package com.dropdrage.simpleweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dropdrage.simpleweather.city_list.presentation.presentation.ui.CityListScreen
import com.dropdrage.simpleweather.city_search.presentation.ui.CitySearchScreen
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.settings.presentation.ui.SettingsScreen
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.cities_weather.CitiesWeatherScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val currentEntry by navController.currentBackStackEntryAsState()

            ComposeMaterial3Theme {
                Scaffold(topBar = {
                    val currentRoute = currentEntry?.destination?.route
                    val currentRouteTitle = Screen.getTitleResOfRoute(currentRoute)
                    AnimatedVisibility(visible = currentRouteTitle != null) {
                        val routeTitle by remember { mutableStateOf(currentRouteTitle!!) }
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = routeTitle),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = navController::navigateUp) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                }
                            }
                        )
                    }
                }) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CITIES_WEATHER.route,
                        modifier = Modifier.padding(it)
                    ) {
                        composable(Screen.CITY_LIST) {
                            CityListScreen(openSearchScreen = { navController.navigate(route = Screen.CITY_SEARCH) })
                        }
                        composable(Screen.CITY_SEARCH) {
                            CitySearchScreen(navigateBack = navController::navigateUp)
                        }
                        composable(Screen.SETTINGS) {
                            SettingsScreen()
                        }
                        composable(Screen.CITIES_WEATHER) {
                            CitiesWeatherScreen(
                                navigateCityList = { navController.navigate(route = Screen.CITY_LIST) },
                                navigateSettings = { navController.navigate(route = Screen.SETTINGS) }
                            )
                        }
                    }
                }
            }
        }
    }
}
