package com.arbuzerxxl.vibeshot.core.design.theme


import androidx.annotation.FontRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contraomnese.weather.design.DevicePreviews
import com.contraomnese.weather.design.R


fun interface Buildable {
    fun build(@FontRes font: Int): TextStyle
}

@OptIn(ExperimentalTextApi::class)
private fun createFont(@FontRes font: Int, weight: Int, opticalSize: Int): Font {
    return Font(
        resId = font,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(weight),
            FontVariation.opticalSizing(opticalSize.sp)
        )
    )
}

private fun createTextStyle(
    font: Font,
    fontSize: Int? = null,
    letterSpacing: Float? = null,
    lineHeight: Float? = null,
): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(font),
        fontSize = fontSize?.sp ?: TextUnit.Unspecified,
        letterSpacing = letterSpacing?.sp ?: TextUnit.Unspecified,
        lineHeight = lineHeight?.sp ?: TextUnit.Unspecified,
    )
}

object DisplayLargeConfig : Buildable {
    private const val WEIGHT = 100
    private const val OPTICAL_SIZING = 102
    private const val LETTER_SPACING = -0.5f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING
    )
}

object DisplayMediumConfig : Buildable {
    private const val WEIGHT = 300
    private const val OPTICAL_SIZING = 53
    private const val LETTER_SPACING = 10.5f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING
    )
}

object DisplaySmallConfig : Buildable {
    private const val WEIGHT = 400
    private const val OPTICAL_SIZING = 36
    private const val LETTER_SPACING = .0f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING
    )
}

object HeadlineLargeConfig : Buildable {
    private const val WEIGHT = 400
    private const val OPTICAL_SIZING = 37
    private const val LETTER_SPACING = -0.5f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING
    )
}

object HeadlineMediumConfig : Buildable {
    private const val WEIGHT = 500
    private const val OPTICAL_SIZING = 22

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
    )
}

object TitleLargeConfig : Buildable {
    private const val WEIGHT = 700
    private const val OPTICAL_SIZING = 25
    private const val LETTER_SPACING = -1.0f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING
    )
}

object TitleMediumConfig : Buildable {
    private const val WEIGHT = 500
    private const val OPTICAL_SIZING = 15

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
    )
}

object BodyLargeConfig : Buildable {
    private const val WEIGHT = 500
    private const val OPTICAL_SIZING = 22

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
    )
}

object BodyMediumConfig : Buildable {
    private const val WEIGHT = 400
    private const val OPTICAL_SIZING = 18
    private const val LETTER_SPACING = 4.0f
    private const val LINE_HEIGHT = 20.0f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING,
        lineHeight = LINE_HEIGHT
    )
}

object BodySmallConfig : Buildable {
    private const val WEIGHT = 400
    private const val OPTICAL_SIZING = 17
    private const val LETTER_SPACING = 3.0f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING,
    )
}

object LabelMediumConfig : Buildable {
    private const val WEIGHT = 400
    private const val OPTICAL_SIZING = 19

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
    )
}

object LabelSmallConfig : Buildable {
    private const val WEIGHT = 400
    private const val OPTICAL_SIZING = 15
    private const val LETTER_SPACING = 5.0f
    private const val LINE_HEIGHT = 20.0f

    override fun build(@FontRes font: Int): TextStyle = createTextStyle(
        font = createFont(font = font, weight = WEIGHT, opticalSize = OPTICAL_SIZING),
        fontSize = OPTICAL_SIZING,
        letterSpacing = LETTER_SPACING,
        lineHeight = LINE_HEIGHT
    )
}

val weatherTypography = Typography(
    displayLarge = DisplayLargeConfig.build(R.font.inter_variable),
    displayMedium = DisplayMediumConfig.build(R.font.inter_variable),
    displaySmall = DisplaySmallConfig.build(R.font.inter_variable),
    headlineLarge = HeadlineLargeConfig.build(R.font.inter_variable),
    headlineMedium = HeadlineMediumConfig.build(R.font.inter_variable),
    titleLarge = TitleLargeConfig.build(R.font.inter_variable), //
    titleMedium = TitleMediumConfig.build(R.font.inter_variable),
    bodyLarge = BodyLargeConfig.build(R.font.inter_variable), //
    bodyMedium = BodyMediumConfig.build(R.font.inter_variable), //
    bodySmall = BodySmallConfig.build(R.font.inter_variable), //
    labelMedium = LabelMediumConfig.build(R.font.inter_variable), //
    labelSmall = LabelSmallConfig.build(R.font.inter_variable) //
)

@DevicePreviews
@Composable
fun TypographyPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding8)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(space8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "21°",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "21°",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "6:28 AM",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Seongnam-si",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "21° | Partly Cloudy",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "My Location",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Surface (
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(cornerSize8)),
                tonalElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            ) {
                Text(
                    "10-DAY  FORECAST",
                    modifier = Modifier.padding(padding8),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                )
            }
            Text(
                "Today 21°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Low for the rest of the day",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Column (
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(cornerSize8))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
            ) {
                Text(
                    "Cloudy conditions from 1AM-9AM, with showers expected at 9AM.",
                    modifier = Modifier.padding(padding8),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "60%",
                    modifier = Modifier.padding(padding8),
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = TextUnit.Unspecified,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            TextField(
                value = "Search for a city or airport",
                onValueChange = {},
                textStyle = MaterialTheme.typography.labelMedium,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(cornerSize16)
            )
            Text(
                "H:29° L:15°",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 21.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

        }
    }
}