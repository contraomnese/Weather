package com.contraomnese.weather.weatherByLocation.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.DailyForecastItem
import com.contraomnese.weather.core.ui.widgets.HourlyForecastRow
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.WeatherSection
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.padding12
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.design.theme.space32
import com.contraomnese.weather.design.theme.space40
import kotlin.math.min

@Composable
internal fun WeatherRoute(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> LoadingIndicator(modifier = Modifier.height(itemHeight64))
        else -> WeatherScreen(
            uiState = uiState, modifier = modifier
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WeatherScreen(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = padding16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space40)
        ) {

            item {
                Text(
                    modifier = Modifier
                        .padding(top = padding12),
                    text = uiState.location.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Text(
                    modifier = Modifier
                        .padding(top = padding8),
                    text = uiState.weather!!.currentTemperature,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            stickyHeader {
                WeatherSection(title = "ПОЧАСОВОЙ ПРОГНОЗ") {
                    HourlyForecastRow(uiState.hourlyForecastStub)
                }
            }

            item { Spacer(modifier = Modifier.height(space16)) }

            stickyHeader {
                WeatherSection(
                    title = "ПРОГНОЗ НА 10 ДНЕЙ",
                    modifier = Modifier.zIndex(1f)
                ) {}
            }

            items(uiState.dailyForecastStub) { (day, temp) ->
                DailyForecastItem(day, temp)
            }

            item {
                Spacer(modifier = Modifier.height(space32))
            }
        }
    }
}

@Composable
fun MainScreen() {
    NestedScrollExample()
}

@Composable
fun CustomStickyNoVerticalScroll() {
    var scrollOffset by remember { mutableFloatStateOf(0f) }

    val configuration = LocalConfiguration.current

    val screenHeightPx = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }

    val headerHeightPx = with(LocalDensity.current) { 40.dp.toPx() }

    val forecastBodyHeightPx = with(LocalDensity.current) { 150.dp.toPx() }
    val dailyBodyHeightPx = with(LocalDensity.current) { 300.dp.toPx() }

    SubcomposeLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = padding8)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    scrollOffset = (scrollOffset - dragAmount.y)
                        .coerceAtLeast(0f)
                    Log.d("CollapsibleContainer", "scrollOffset -- ${scrollOffset}")
                }
            }
    ) { constraints ->

        val forecastCurrentBodyHeight = forecastBodyHeightPx - min(scrollOffset, forecastBodyHeightPx)
        val dailyCurrentBodyHeight =
            dailyBodyHeightPx - min(scrollOffset - headerHeightPx - dailyBodyHeightPx, dailyBodyHeightPx)

        val forecastHeaderPlaceable = subcompose("forecastHeader") {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { headerHeightPx.toDp() })
                    .clip(RoundedCornerShape(topStart = cornerRadius16, topEnd = cornerRadius16))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) { Text("FORECAST", color = Color.White) }
        }[0].measure(Constraints.fixedWidth(constraints.maxWidth))

        val forecastBodyPlaceable = if (forecastCurrentBodyHeight > 0f) {
            subcompose("forecastBody") {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(with(LocalDensity.current) { forecastCurrentBodyHeight.toDp() })
                        .clip(RoundedCornerShape(bottomStart = cornerRadius16, bottomEnd = cornerRadius16))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
                )
            }[0].measure(Constraints.fixedWidth(constraints.maxWidth))
        } else null

        val dailyHeaderPlaceable = subcompose("dailyHeader") {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { headerHeightPx.toDp() })
                    .padding(top = padding8)
                    .clip(RoundedCornerShape(topStart = cornerRadius16, topEnd = cornerRadius16))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) { Text("DAILY", color = Color.White) }
        }[0].measure(Constraints.fixedWidth(constraints.maxWidth))

        val dailyBodyPlaceable = if (dailyCurrentBodyHeight > 0f) {
            subcompose("dailyBody") {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(with(LocalDensity.current) { dailyCurrentBodyHeight.toDp() })
                        .clip(RoundedCornerShape(bottomStart = cornerRadius16, bottomEnd = cornerRadius16))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
                )
            }[0].measure(Constraints.fixedWidth(constraints.maxWidth))
        } else null


        val totalHeight =
            headerHeightPx + (forecastBodyPlaceable?.height ?: 0) + headerHeightPx + (dailyBodyPlaceable?.height ?: 0)

        val forecastHeaderOffset = if (forecastCurrentBodyHeight == 0f) {
            -min(
                scrollOffset - forecastBodyHeightPx,
                if (totalHeight > screenHeightPx) totalHeight - screenHeightPx else 0f
            )
        } else {
            0f
        }

        val dailyHeaderOffset = if (dailyCurrentBodyHeight == 0f) {
            -min(
                scrollOffset - dailyBodyHeightPx,
                if (totalHeight > screenHeightPx) totalHeight - screenHeightPx else headerHeightPx + forecastBodyHeightPx
            )
        } else {
            (forecastHeaderPlaceable.height).toFloat() + (forecastBodyPlaceable?.height?.toFloat() ?: 0f)
        }

        Log.d("CollapsibleContainer", "totalHeight -- ${totalHeight}")
        Log.d("CollapsibleContainer", "headerOffset -- ${forecastHeaderOffset}")

        layout(constraints.maxWidth, totalHeight.toInt()) {

            forecastHeaderPlaceable.place(0, forecastHeaderOffset.toInt())

            val forecastBodyY = (forecastHeaderOffset + forecastHeaderPlaceable.height).toInt()
            forecastBodyPlaceable?.place(0, forecastBodyY)

            dailyHeaderPlaceable.place(0, dailyHeaderOffset.toInt())

            val dailyBodyY = forecastBodyY + (dailyHeaderOffset + dailyHeaderPlaceable.height).toInt()
            dailyBodyPlaceable?.place(0, dailyBodyY)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        MainScreen()
    }
}