package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun PrecipitationProbabilityIcon(
    modifier: Modifier,
    precipitationProbability: Int,
    textSize: Dp,
) {

    val density = LocalDensity.current
    Text(
        modifier = modifier,
        text = "$precipitationProbability%",
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = with(density) { (textSize * .25f).toSp() }
        ),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}


@Preview
@Composable
fun PrecipitationProbabilityIconPreview() {
    WeatherTheme {

        PrecipitationProbabilityIcon(
            modifier = Modifier.size(128.dp),
            precipitationProbability = 20,
            textSize = 32.dp
        )

    }
}