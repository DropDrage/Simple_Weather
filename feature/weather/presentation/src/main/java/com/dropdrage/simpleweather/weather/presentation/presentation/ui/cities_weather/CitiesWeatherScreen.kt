package com.dropdrage.simpleweather.weather.presentation.presentation.ui.cities_weather

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dropdrage.simpleweather.core.presentation.ui.TextWithSubtext
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Small100
import com.dropdrage.simpleweather.core.style.Small50
import com.dropdrage.simpleweather.weather.presentation.R
import com.dropdrage.simpleweather.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.city.weather.city.CityWeatherScreen
import com.dropdrage.simpleweather.weather.presentation.presentation.ui.city.weather.current_location.CurrentLocationWeatherScreen
import com.dropdrage.simpleweather.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.simpleweather.weather.presentation.ui.cities_weather.CitiesWeatherViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CitiesWeatherScreen(navigateCityList: () -> Unit, navigateSettings: () -> Unit) {

    val viewModel = viewModel<CitiesWeatherViewModel>()
    val citiesSharedViewModel = viewModel<CitiesSharedViewModel>()

    val cityTitle by citiesSharedViewModel.currentCityTitle.collectAsState(initial = ViewCityTitle("Default", "DF"))
    val cities by viewModel.cities.collectAsState(initial = emptyList())

    val toolbarState = rememberCollapsingToolbarScaffoldState()
    val pagerState = rememberPagerState()

    val toolbarProgress = toolbarState.toolbarState.progress

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

@OptIn(ExperimentalPagerApi::class)
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
                contentDescription = stringResource(id = R.string.action_add_city),
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
        Row() {
            AppBarMenu(navigateCityList = {}, navigateSettings = {})
        }
    }
}

/*
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:fitsSystemWindows="true">

<com.google.android.material.appbar.AppBarLayout
android:id="@+id/appBar"
android:layout_width="match_parent"
android:layout_height="108dp"
android:theme="@style/Theme.AppBar.Primary">

<com.google.android.material.appbar.SubtitleCollapsingToolbarLayout
android:id="@+id/collapsingToolbar"
android:layout_width="match_parent"
android:layout_height="match_parent"
app:collapsedSubtitleTextAppearance="?textAppearanceBodySmall"
app:collapsedTitleGravity="center_vertical"
app:collapsedTitleTextAppearance="?textAppearanceBodyLarge"
app:collapsedTitleTextColor="?colorPrimary"
app:expandedSubtitleTextAppearance="?textAppearanceBodySmall"
app:expandedTitleGravity="top"
app:expandedTitleMarginBottom="0dp"
app:expandedTitleMarginEnd="@dimen/toolbar_expanded_margin_end"
app:expandedTitleMarginStart="@dimen/medium_100"
app:expandedTitleMarginTop="@dimen/small_150"
app:expandedTitleTextAppearance="?textAppearanceHeadlineLarge"
app:expandedTitleTextColor="?colorPrimary"
app:layout_scrollFlags="scroll|exitUntilCollapsed"
app:scrimVisibleHeightTrigger="@dimen/huge_100"
app:titleCollapseMode="scale"
app:toolbarId="@id/toolbar">

<com.google.android.material.appbar.MaterialToolbar
android:id="@+id/toolbar"
android:layout_width="match_parent"
android:layout_height="?attr/actionBarSize"
app:layout_collapseMode="pin"
app:layout_scrollFlags="scroll|enterAlways"
app:titleMargin="0dp"
tools:subtitle="CY"
tools:title="Long long long long long city" />

<com.google.android.material.tabs.TabLayout
android:id="@+id/tabs"
android:layout_width="wrap_content"
android:layout_height="@dimen/medium_100"
android:layout_marginStart="@dimen/medium_100"
android:layout_marginTop="@dimen/huge_112"
android:backgroundTint="@android:color/transparent"
app:layout_collapseMode="parallax"
app:tabBackground="@drawable/ic_dot"
app:tabGravity="center"
app:tabIndicator="@null"
app:tabIndicatorGravity="center"
app:tabMaxWidth="@dimen/width_city_tab"
app:tabPaddingEnd="@dimen/small_75"
app:tabPaddingStart="@dimen/small_75" />

</com.google.android.material.appbar.SubtitleCollapsingToolbarLayout>
</com.google.android.material.appbar.AppBarLayout>

<androidx.viewpager2.widget.ViewPager2
android:id="@+id/citiesWeather"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:nestedScrollingEnabled="false"
app:layout_behavior="@string/appbar_scrolling_view_behavior" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>*/
