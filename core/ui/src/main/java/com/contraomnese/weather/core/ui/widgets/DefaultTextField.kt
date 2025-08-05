package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius10
import com.contraomnese.weather.design.theme.itemHeight40
import com.contraomnese.weather.design.theme.itemThickness1
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding4


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    onTextFieldFocusChanged: (Boolean) -> Unit = {},
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Go),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = defaultFieldColors(),
) {

    val focusRequester = remember { FocusRequester() }
    val currentKeyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onTextFieldFocusChanged(isFocused)
    }

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChanged(it) },
            modifier = modifier
                .height(itemHeight40)
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            singleLine = singleLine,
            maxLines = 1,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                color = when {
                    isError -> MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    isFocused -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                }
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onGo = {
                    focusManager.clearFocus(force = true)
                    currentKeyboardController?.hide()
                }
            ),
            interactionSource = interactionSource,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface.copy(0.5f)),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    isError = isError,
                    singleLine = singleLine,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon ?: {
                        if (value.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onValueChanged("")
                                },
                            ) {
                                Icon(
                                    imageVector = WeatherIcons.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    placeholder = { PlaceholderText(placeholder) },
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = leadingIcon?.let { PaddingValues(horizontal = padding4) }
                        ?: PaddingValues(horizontal = padding16),
                    container = {
                        TextFieldDefaults.Container(
                            enabled = enabled,
                            isError = false,
                            colors = colors,
                            interactionSource = interactionSource,
                            shape = RoundedCornerShape(cornerRadius10),
                            focusedIndicatorLineThickness = itemThickness2,
                            unfocusedIndicatorLineThickness = itemThickness1
                        )
                    }
                )
            }
        )
    }
}

@Composable
private fun PlaceholderText(placeholder: String) {
    Text(
        text = placeholder,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W400),
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    )
}

@Composable
private fun defaultFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
    unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.8f),
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.8f),
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    errorLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.8f),
    errorTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.8f),
)

@Preview
@Composable
private fun TextFieldPlaceholderPreview() {
    WeatherTheme {
        DefaultTextField(
            value = "",
            onValueChanged = {},
            isError = false,
            singleLine = true,
            placeholder = "Placeholder",
        )
    }
}

@Preview
@Composable
private fun TextFieldValuePreview() {
    WeatherTheme {
        DefaultTextField(
            value = "Value",
            onValueChanged = {},
            isError = false,
            singleLine = true,
        )
    }
}

@Preview
@Composable
private fun TextFieldIsErrorPreview() {
    WeatherTheme {
        DefaultTextField(
            value = "03-1",
            onValueChanged = {},
            isError = true,
            singleLine = true,
        )
    }
}