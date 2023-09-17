package com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.whenStarted
import com.dropdrage.simpleweather.core.presentation.ui.TextWithSubtext
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Small100
import com.dropdrage.simpleweather.core.style.Small50
import com.dropdrage.simpleweather.feature.weather.R
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.weather.city.CityWeatherScreen
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.weather.current_location.CurrentLocationWeatherScreen
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

private const val LOCATION_FRAGMENT_POSITION = 0
private const val LOCATION_FRAGMENT_COUNT = 1

private val DOT_SIZE = 6.dp

private const val collapsed = 16
private const val expanded = 32
private const val difference = expanded - collapsed

@Composable
fun CitiesWeatherScreen(navigateCityList: () -> Unit, navigateSettings: () -> Unit) {
    val localLifecycleOwner = LocalLifecycleOwner.current

    val viewModel = hiltViewModel<CitiesWeatherViewModel>()
    val citiesSharedViewModel = hiltViewModel<CitiesSharedViewModel>()

    val cityTitle by citiesSharedViewModel.currentCityTitle.collectAsState(initial = ViewCityTitle("Default", "DF"))
    val cities by viewModel.cities.collectAsState(initial = emptyList())

    val toolbarState = rememberCollapsingToolbarScaffoldState()
    val pagerState = rememberPagerState()

    val toolbarProgress = toolbarState.toolbarState.progress

    LaunchedEffect(key1 = localLifecycleOwner) {
        localLifecycleOwner.whenStarted {
            println("Started")
            viewModel.updateWeather()
        }
    }

    CollapsingToolbarScaffold(
        toolbar = {
            Toolbar(
                cityTitle = cityTitle,
                toolbarProgress = toolbarProgress,
                pagerState = pagerState,
                navigateCityList = navigateCityList,
                navigateSettings = navigateSettings
            )
        },
        state = toolbarState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        modifier = Modifier.fillMaxSize(),
        toolbarModifier = Modifier.padding(top = Small100, bottom = Small100, start = Medium100, end = Small100)
    ) {
        HorizontalPager(
            count = cities.size + LOCATION_FRAGMENT_COUNT,
            key = { it - LOCATION_FRAGMENT_COUNT },
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {
            val isVisible = pagerState.currentPage == it
            if (it != LOCATION_FRAGMENT_POSITION) {
                CityWeatherScreen(
                    isVisible = isVisible,
                    order = it - LOCATION_FRAGMENT_COUNT,
                    citiesSharedViewModel = citiesSharedViewModel
                )
            } else {
                CurrentLocationWeatherScreen(
                    isVisible = isVisible,
                    citiesSharedViewModel = citiesSharedViewModel
                )
            }
        }
    }
}

@Composable
private fun Toolbar(
    cityTitle: ViewCityTitle,
    toolbarProgress: Float,
    pagerState: PagerState,
    navigateCityList: () -> Unit,
    navigateSettings: () -> Unit,
) {
    val localContext = LocalContext.current

    val topAppBarTextSize = (collapsed + difference * toolbarProgress).sp

    Spacer(modifier = Modifier.height(48.dp))
    Spacer(modifier = Modifier.height(76.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = Small50)
        ) {
            TextWithSubtext(
                text = cityTitle.city.getMessage(localContext),
                textStyle = MaterialTheme.typography.titleLarge,
                textFontSize = topAppBarTextSize,
                subtext = cityTitle.countryCode.getMessage(localContext),
                subtextStyle = MaterialTheme.typography.labelMedium,
            )

            if (toolbarProgress > 0) {
                Spacer(modifier = Modifier.height(Small50 * toolbarProgress))

                DottedTabsIndicator(
                    currentPage = pagerState.currentPage,
                    pagesCount = pagerState.pageCount,
                    modifier = Modifier
                        .height(DOT_SIZE * toolbarProgress)
                        .alpha(toolbarProgress * toolbarProgress)
                )
            }
        }

        AppBarMenu(navigateCityList = navigateCityList, navigateSettings = navigateSettings)
    }
}

@Composable
private fun DottedTabsIndicator(currentPage: Int, pagesCount: Int, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(Small50), modifier = modifier) {
        val inactiveDotModifier = Modifier
            .size(DOT_SIZE)
            .border(1.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape)

        repeat(pagesCount) {
            if (it != currentPage) {
                Box(modifier = inactiveDotModifier)
            } else {
                Box(
                    modifier = Modifier
                        .size(DOT_SIZE)
                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
private fun AppBarMenu(navigateCityList: () -> Unit, navigateSettings: () -> Unit, modifier: Modifier = Modifier) {
    var isOptionsShown by remember {
        mutableStateOf(false)
    }

    Row(modifier = modifier) {
        IconButton(onClick = navigateCityList) {
            Icon(
                painter = painterResource(id = R.drawable.ic_city),
                contentDescription = stringResource(id = R.string.action_city_list),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(onClick = { isOptionsShown = !isOptionsShown }) {
            Icon(Icons.Default.MoreVert, "", tint = MaterialTheme.colorScheme.primary)
        }

        DropdownMenu(
            expanded = isOptionsShown,
            onDismissRequest = { isOptionsShown = false }
        ) {
            DropdownMenuItem(
                onClick = navigateSettings,
                text = {
                    Text(
                        text = stringResource(id = R.string.action_settings),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DottedTabsIndicatorPreview() {
    ComposeMaterial3Theme {
        DottedTabsIndicator(1, 4)
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBarMenuPreview() {
    ComposeMaterial3Theme {
        Row {
            AppBarMenu(navigateCityList = {}, navigateSettings = {})
        }
    }
}
