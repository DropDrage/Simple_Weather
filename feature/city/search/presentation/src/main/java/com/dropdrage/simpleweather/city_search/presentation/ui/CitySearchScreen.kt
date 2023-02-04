package com.dropdrage.simpleweather.city_search.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.dropdrage.common.presentation.utils.collectInLaunchedEffect
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.city_search.presentation.R
import com.dropdrage.simpleweather.city_search.presentation.model.ViewCitySearchResult
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.core.presentation.ui.TextWithSubtext
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Medium150
import com.dropdrage.simpleweather.core.style.Small150

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchScreen(navigateBack: () -> Unit) {
    val viewModel = hiltViewModel<CitySearchViewModel>()

    val focusRequester = remember { FocusRequester() }

    val query by viewModel.query.collectAsState(initial = "")

    val searchResults = viewModel.searchResults.collectAsState(initial = emptyList())
    viewModel.cityAddedEvent.collectInLaunchedEffect { navigateBack() }

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                updateQuery = viewModel::updateQuery,
                navigateBack = navigateBack,
                modifier = Modifier.fillMaxWidth(),
                queryInputModifier = Modifier
                    .focusRequester(focusRequester)
                    .onGloballyPositioned { focusRequester.requestFocus() }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            items(searchResults.value, key = { "${it.city.location.latitude}/${it.city.location.latitude}" }) {
                CitySearchItem(
                    item = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.addCity(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    updateQuery: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    queryInputModifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = updateQuery,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.city_search_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { updateQuery("") }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier.size(Medium150)
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = queryInputModifier.fillMaxWidth(),
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(Medium150)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun CitySearchItem(item: ViewCitySearchResult, modifier: Modifier = Modifier) {
    TextWithSubtext(
        text = item.name,
        subtext = item.countryName,
        textStyle = MaterialTheme.typography.bodyLarge,
        subtextStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(vertical = Small150, horizontal = Medium100),
    )
}


@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    ComposeMaterial3Theme {
        SearchBar(query = "Query", updateQuery = {}, navigateBack = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarEmptyPreview() {
    ComposeMaterial3Theme {
        SearchBar(query = "", updateQuery = {}, navigateBack = { })
    }
}


@Preview(showBackground = true)
@Composable
private fun CitySearchPreview() {
    ComposeMaterial3Theme {
        CitySearchItem(
            item = ViewCitySearchResult(City("City", Location(0f, 0f), Country("Country", "CY"))),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
