package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dropdrage.common.presentation.utils.collectInLaunchedEffect
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.city_list.presentation.R
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.swap
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.ui.TextWithSubtext
import com.dropdrage.simpleweather.core.presentation.ui.WeatherWithTemperature
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Large100
import com.dropdrage.simpleweather.core.style.Large150
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Medium150
import com.dropdrage.simpleweather.core.style.Small100
import com.dropdrage.simpleweather.core.style.Small50

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(openSearchScreen: () -> Unit) {
    val localDensity = LocalDensity.current

    val viewModel = hiltViewModel<CityListViewModel>()

    var citiesCurrentWeather = remember { mutableStateListOf<ViewCityCurrentWeather>() }

    viewModel.citiesCurrentWeathers.collectInLaunchedEffect {
        citiesCurrentWeather.clear()
        citiesCurrentWeather.addAll(it)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = openSearchScreen,
                modifier = Modifier.padding(end = Small100, bottom = Medium150)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(id = R.string.city_list_add_city)
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        DragDropList(
            items = citiesCurrentWeather,
            onMove = { from, to -> citiesCurrentWeather.swap(from, to) },
            onDragEnd = { viewModel.saveOrder(citiesCurrentWeather) },
            keyProducer = { item -> "${item.city.location.latitude}|${item.city.location.longitude}" },
            modifier = Modifier.fillMaxSize()
        ) { item, isDragging, modifier, draggableModifier ->
            CityItemWithDropdown(
                item = item,
                isDragging = isDragging,
                onDeleteClicked = { viewModel.deleteCity(item.city) },
                localDensity = localDensity,
                modifier = modifier,
                draggableModifier = draggableModifier,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.CityItemWithDropdown(
    item: ViewCityCurrentWeather,
    isDragging: Boolean,
    onDeleteClicked: () -> Unit,
    localDensity: Density,
    modifier: Modifier, draggableModifier: Modifier,
) {
    var isPopupShown by remember {
        mutableStateOf(false)
    }

    val boxModifier = Modifier

    BoxWithConstraints(
        contentAlignment = Alignment.BottomEnd,
        modifier = if (isDragging) boxModifier else boxModifier.animateItemPlacement(tween())
    ) {
        CityItem(
            cityCurrentWeather = item,
            draggableModifier = draggableModifier,
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(onClick = {}, onLongClick = { isPopupShown = true }),
        )

        DeletePopup(
            isPopupShown = isPopupShown,
            onDismissRequest = { isPopupShown = false },
            onDeleteClicked = onDeleteClicked,
            parentWidth = maxWidth,
            localDensity = localDensity
        )
    }
}

@Composable
private fun CityItem(
    cityCurrentWeather: ViewCityCurrentWeather,
    draggableModifier: Modifier,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Medium100),
        ) {
            Box(
                modifier = draggableModifier
                    .fillMaxHeight()
                    .padding(bottom = Medium100)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drag),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .width(Medium150)
                        .fillMaxHeight()
                )
            }
            TextWithSubtext(
                text = cityCurrentWeather.city.name,
                textStyle = MaterialTheme.typography.headlineLarge,
                subtext = cityCurrentWeather.city.country.name,
                subtextStyle = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
            )
            WeatherWithTemperature(
                weatherType = cityCurrentWeather.currentWeather.weatherType,
                temperature = cityCurrentWeather.currentWeather.temperature,
                iconSize = Large150,
                spacing = Small50,
                temperatureTextStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                ),
            )
        }

        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun DeletePopup(
    isPopupShown: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteClicked: () -> Unit,
    parentWidth: Dp,
    modifier: Modifier = Modifier,
    localDensity: Density,
) {
    var dropdownWidth by remember {
        mutableStateOf(0.dp)
    }

    DropdownMenu(
        expanded = isPopupShown,
        onDismissRequest = onDismissRequest,
        modifier = modifier.onGloballyPositioned {
            dropdownWidth = with(localDensity) { it.size.width.toDp() }
        },
        offset = DpOffset(x = parentWidth - dropdownWidth - Large100, y = -Medium100),
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(id = R.string.delete),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = onDeleteClicked,
        )
    }
}


@Preview(showBackground = true, widthDp = 300, heightDp = 128)
@Composable
private fun CityItemPreview() {
    ComposeMaterial3Theme {
        CityItem(
            cityCurrentWeather = ViewCityCurrentWeather(
                City(
                    "City",
                    Location(0f, 0f),
                    Country("Country", "CY")
                ),
                ViewCurrentWeather("37F", ViewWeatherType.ClearSky)
            ),
            draggableModifier = Modifier,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
