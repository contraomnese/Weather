package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.contraomnese.weather.design.DevicePreviews
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    isError: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "Search for a city",
) {

    DefaultTextField(
        value = searchQuery,
        onValueChanged = onSearchQueryChanged,
        onTextFieldFocusChanged = onSearchFocusChanged,
        isError = isError,
        enabled = enabled,
        placeholder = placeholder,
        leadingIcon = {
            Icon(
                imageVector = WeatherIcons.Search,
                contentDescription = stringResource(
                    id = R.string.search_city,
                ),
            )
        },
        modifier = modifier
    )
}

@DevicePreviews
@Composable
private fun SearchTextFieldPreview() {
    WeatherTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            SearchTextField(
                searchQuery = "London",
                modifier = Modifier.padding(padding16)
            )
        }
    }
}