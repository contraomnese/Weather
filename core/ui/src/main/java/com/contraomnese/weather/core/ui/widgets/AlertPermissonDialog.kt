package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemThickness1
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.space16
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onPermissionGranted: (Boolean) -> Unit = {},
    @StringRes deniedTitle: Int,
    @StringRes firstTimeTitle: Int,
    permission: String,
) {

    val permissionState = rememberPermissionState(
        permission, onPermissionGranted
    )

    var confirmed by remember { mutableStateOf(false) }

    LaunchedEffect(confirmed) {
        if (confirmed) {
            permissionState.launchPermissionRequest()
            confirmed = false
        }
    }

    LaunchedEffect(Unit) {
        onPermissionGranted(permissionState.status.isGranted)
    }

    AnimatedVisibility(
        visible = !permissionState.status.isGranted,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        PermissionDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            showRationale = permissionState.status.shouldShowRationale,
            deniedTitle = deniedTitle,
            firstTimeTitle = firstTimeTitle,
            onClick = { confirmed = true })
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PermissionDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    showRationale: Boolean,
    @StringRes deniedTitle: Int,
    @StringRes firstTimeTitle: Int,
    onClick: () -> Unit = {},
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = padding16)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(padding16)) {
                Text(
                    text = if (showRationale) {
                        stringResource(deniedTitle)
                    } else stringResource(firstTimeTitle),
                    style = MaterialTheme.typography.titleMedium.copy(
                        lineBreak = LineBreak.Heading,
                    )
                )
                Spacer(modifier = Modifier.height(space16))
                TextButton(
                    onClick = onClick,
                    modifier = Modifier.align(Alignment.End),
                    border = BorderStroke(itemThickness1, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = stringResource(R.string.confirm_title),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Preview(locale = "ru")
@Composable
private fun AlertPermissionDialogPreview() {
    WeatherTheme {
        PermissionDialog(
            showRationale = true,
            deniedTitle = R.string.permission_location_denied_title,
            firstTimeTitle = R.string.permission_location_first_time_title,
        )
    }
}