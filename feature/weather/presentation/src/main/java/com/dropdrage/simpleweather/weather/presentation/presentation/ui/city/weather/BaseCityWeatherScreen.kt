package com.dropdrage.simpleweather.weather.presentation.presentation.ui.city.weather

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dropdrage.common.domain.Range
import com.dropdrage.common.presentation.utils.collectInLaunchedEffect
import com.dropdrage.common.presentation.utils.showToast
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.ui.WeatherWithTemperature
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Huge112
import com.dropdrage.simpleweather.core.style.Large100
import com.dropdrage.simpleweather.core.style.Large175
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Medium150
import com.dropdrage.simpleweather.core.style.Small100
import com.dropdrage.simpleweather.core.style.Small150
import com.dropdrage.simpleweather.core.style.Small50
import com.dropdrage.simpleweather.weather.presentation.R
import com.dropdrage.simpleweather.weather.presentation.model.ViewCurrentDayWeather
import com.dropdrage.simpleweather.weather.presentation.model.ViewCurrentHourWeather
import com.dropdrage.simpleweather.weather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.weather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.view.SunTimes
import com.dropdrage.simpleweather.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.BaseCityWeatherViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private val BORDER_THIN = 1.dp

private val SIZE_WEATHER_ICON = Huge112
private val SIZE_WEATHER_METRIC_ICON = Large175
private val SIZE_WEATHER_LIST_ICON = Large100

@Composable
internal fun <VM : BaseCityWeatherViewModel> BaseCityWeatherScreen(
    isVisible: Boolean,
    viewModel: VM,
    citiesSharedViewModel: CitiesSharedViewModel,
) {
    val localContext = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }

    val hourlyWeatherListState = rememberLazyListState()

    val currentHourWeather by viewModel.currentHourWeather.collectAsState(
        initial = ViewCurrentHourWeather(
            weatherType = ViewWeatherType.ClearSky,
            temperature = "--/--",
            "",
            "",
            "",
            "",
        )
    )
    val currentDayWeather by viewModel.currentDayWeather.collectAsState(
        initial = ViewCurrentDayWeather(
            weatherType = ViewWeatherType.ClearSky,
            temperatureRange = Range("", ""),
            apparentTemperatureRange = Range("", ""),
            precipitationSum = "",
            maxWindSpeed = "",
            sunriseTime = LocalDateTime.of(LocalDate.now(), LocalTime.NOON),
            sunriseFormatted = "12:00",
            sunsetTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT),
            sunsetFormatted = "00:00"
        )
    )
    val hourlyWeather by viewModel.hourlyWeather.collectAsState(initial = emptyList())
    val dailyWeather by viewModel.dailyWeather.collectAsState(initial = emptyList())


    viewModel.error.collectInLaunchedEffect {
        if (it != null) {
            localContext.showToast(it.getMessage(localContext))
        }
    }
    viewModel.cityTitle.collectInLaunchedEffect(citiesSharedViewModel::setCityTitle)
    viewModel.hourlyWeather.collectInLaunchedEffect {
        if (it.isNotEmpty()) {
            hourlyWeatherListState.scrollToItem(it.indexOfFirst { it.isNow })
        }
    }

    key(isVisible) {
        if (isVisible) {
            viewModel.updateCityName()
            viewModel.loadWeather()
        } else {
            viewModel.clearErrors()
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Medium100)
    ) {
        item {
            CurrentHourWeatherCard(currentHourWeather, modifier = Modifier.fillMaxWidth())
        }

        item {
            Spacer(modifier = Modifier.height(Medium150))
            Title(title = stringResource(id = R.string.weather_today))
        }
        item {
            Spacer(modifier = Modifier.height(Medium100))
            TodayWeatherMetrics(currentDayWeather)
        }
        item {
            Spacer(modifier = Modifier.height(Medium100))
            Card(modifier = Modifier.fillMaxWidth()) {
                SunTimes(
                    primaryColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    arcBackgroundColor = MaterialTheme.colorScheme.background,
                    arcThickness = Small100,
                    arcGroundEdgeMargin = Medium150,
                    timeTextTopMargin = Small100,
                    sunDrawableRes = R.drawable.ic_sun,
                    sunSize = Medium150,
                    sunriseTime = currentDayWeather.sunriseTime,
                    sunsetTime = currentDayWeather.sunsetTime,
                    sunriseFormatted = currentDayWeather.sunriseFormatted,
                    sunsetFormatted = currentDayWeather.sunsetFormatted,
                    modifier = Modifier.padding(Medium100)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(Medium100))
            Card(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Medium100),
                    state = hourlyWeatherListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Medium100, horizontal = Small100)
                ) {
                    items(items = hourlyWeather, key = { it.dateTime.toString() }) {
                        HourWeatherItem(
                            weather = it,
                            modifier = Modifier.clickable(interactionSource = interactionSource, indication = null) {
                                localContext.showToast(it.weatherType.weatherDescriptionRes)
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(Medium150))
            Title(title = stringResource(id = R.string.weather_daily_weather))
        }
        item {
            Spacer(modifier = Modifier.height(Medium100))
            Card(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Medium100),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Medium100, horizontal = Small100)
                ) {
                    items(items = dailyWeather, key = { it.dateFormatted }) {
                        DayWeatherItem(
                            weather = it,
                            modifier = Modifier.clickable(interactionSource = interactionSource, indication = null) {
                                localContext.showToast(it.weatherType.weatherDescriptionRes)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentHourWeatherCard(currentWeather: ViewCurrentHourWeather, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Small100),
        border = BorderStroke(BORDER_THIN, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Medium100)
        ) {
            Spacer(modifier = Modifier.height(Small100))
            WeatherWithTemperature(
                weatherType = currentWeather.weatherType,
                temperature = currentWeather.temperature,
                iconSize = SIZE_WEATHER_ICON,
                spacing = Medium100,
                temperatureTextStyle = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(Small100))
            Text(
                text = stringResource(id = currentWeather.weatherType.weatherDescriptionRes),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(Medium150))
            CurrentHourWeatherValues(
                values = listOf(
                    stringResource(id = R.string.weather_current_pressure),
                    stringResource(id = R.string.weather_current_humidity),
                    stringResource(id = R.string.weather_current_wind),
                    stringResource(id = R.string.weather_current_visibility),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(Small100))
            CurrentHourWeatherValues(
                values = listOf(
                    currentWeather.pressure,
                    currentWeather.humidity,
                    currentWeather.windSpeed,
                    currentWeather.visibility,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CurrentHourWeatherValues(
    values: List<String>,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        values.forEach {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                fontWeight = fontWeight,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Title(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}

@Composable
private fun TodayWeatherMetrics(currentWeather: ViewCurrentDayWeather, modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (temperature, temperatureApparent, precipitation, windSpeed) = createRefs()

        Card(modifier = Modifier
            .padding(end = Small100)
            .constrainAs(temperature) {
                top.linkTo(parent.top)

                width = Dimension.fillToConstraints
            }

        ) {
            WeatherMetric(
                iconPainter = painterResource(id = R.drawable.ic_temperature),
                text = currentWeather.temperatureRange,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Card(modifier = Modifier
            .padding(start = Small100)
            .constrainAs(temperatureApparent) {
                top.linkTo(temperature.top)

                width = Dimension.fillToConstraints
            }
        ) {
            WeatherMetric(
                iconPainter = painterResource(id = R.drawable.ic_temperature_feel),
                text = currentWeather.apparentTemperatureRange,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        createHorizontalChain(temperature, temperatureApparent, chainStyle = ChainStyle.SpreadInside)

        Card(modifier = Modifier
            .constrainAs(precipitation) {
                top.linkTo(temperature.bottom, Medium100)
                start.linkTo(parent.start)

                width = Dimension.fillToConstraints
            }
            .padding(end = Small100)
        ) {
            WeatherMetric(
                iconPainter = painterResource(id = R.drawable.ic_precipitation),
                topText = currentWeather.precipitationSum,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Card(modifier = Modifier
            .constrainAs(windSpeed) {
                top.linkTo(precipitation.top)

                width = Dimension.fillToConstraints
            }
            .padding(start = Small100)
        ) {
            WeatherMetric(
                iconPainter = painterResource(id = R.drawable.ic_wind_speed),
                topText = currentWeather.maxWindSpeed,
                modifier = Modifier.fillMaxWidth()
            )
        }
        createHorizontalChain(precipitation, windSpeed)
    }
}

@Composable
private fun WeatherMetric(iconPainter: Painter, text: Range<String>, modifier: Modifier = Modifier) {
    WeatherMetric(iconPainter = iconPainter, topText = text.end, bottomText = text.start, modifier = modifier)
}

@Composable
private fun WeatherMetric(
    iconPainter: Painter,
    topText: String,
    bottomText: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = Medium100, bottom = Medium100, start = Small100, end = Small150),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(SIZE_WEATHER_METRIC_ICON)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(IntrinsicSize.Max)) {
            Text(
                text = topText,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (bottomText != null) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Small50),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = bottomText,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun HourWeatherItem(weather: ViewHourWeather, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = weather.timeFormatted,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (weather.isNow) FontWeight.SemiBold else null,
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(Small100))
        WeatherWithTemperature(
            weatherType = weather.weatherType,
            temperature = weather.temperature,
            iconSize = SIZE_WEATHER_LIST_ICON,
            spacing = Small100,
            temperatureTextStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun DayWeatherItem(weather: ViewDayWeather, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.width(IntrinsicSize.Max)) {
        Text(
            text = weather.dayTitle,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = weather.dateFormatted,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(Small100))
        Icon(
            painter = painterResource(id = weather.weatherType.iconRes),
            contentDescription = stringResource(id = weather.weatherType.weatherDescriptionRes),
            tint = Color.Unspecified,
            modifier = Modifier.size(SIZE_WEATHER_LIST_ICON),
        )
        Spacer(modifier = Modifier.height(Small100))
        Text(
            text = weather.temperatureRange.end,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium
        )
        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth())
        Text(
            text = weather.temperatureRange.start,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CurrentHourWeatherCardPreview() {
    ComposeMaterial3Theme {
        CurrentHourWeatherCard(
            ViewCurrentHourWeather(
                weatherType = ViewWeatherType.ClearSky,
                temperature = "72.3F",
                "1029hpa",
                "90%",
                "30km/h",
                "900m",
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherMetricPreview() {
    ComposeMaterial3Theme {
        WeatherMetric(
            painterResource(id = R.drawable.ic_temperature),
            topText = "82.1F",
            bottomText = "72.1F",
            modifier = Modifier.width(128.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun TodayWeatherPreview() {
    ComposeMaterial3Theme {
        TodayWeatherMetrics(
            currentWeather = ViewCurrentDayWeather(
                weatherType = ViewWeatherType.ClearSky,
                temperatureRange = Range("82.1F", "72.1F"),
                apparentTemperatureRange = Range("82.1F", "72.1F"),
                precipitationSum = "71mm",
                maxWindSpeed = "32 km/h",
                sunriseTime = LocalDateTime.of(LocalDate.now(), LocalTime.NOON),
                sunriseFormatted = "12:00",
                sunsetTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT),
                sunsetFormatted = "00:00"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HourWeatherItemPreview() {
    ComposeMaterial3Theme {
        HourWeatherItem(
            weather = ViewHourWeather(
                dateTime = LocalDateTime.now(),
                timeFormatted = "10:00", isNow = false, weatherType = ViewWeatherType.ClearSky,
                temperature = "72.1F",
                pressure = "",
                windSpeed = "",
                humidity = "",
                visibility = "",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DayWeatherItemPreview() {
    ComposeMaterial3Theme {
        DayWeatherItem(
            weather = ViewDayWeather(
                date = LocalDate.now(),
                dayTitle = "Today",
                dateFormatted = "23.12",
                weatherType = ViewWeatherType.ClearSky,
                temperatureRange = Range("72.1F", "92.1F")
            )
        )
    }
}
