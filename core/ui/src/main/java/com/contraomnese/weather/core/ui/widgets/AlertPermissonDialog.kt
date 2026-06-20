package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding32
import com.contraomnese.weather.design.theme.padding80
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
    isSelectable: Boolean = false,
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
            isSelectable,
            onConfirmClick = { confirmed = true },
            onGrantClick = { confirmed = true },
            onNoGrantClick = {
                confirmed = false
                onDismissRequest()
            }
        )
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
    isSelectable: Boolean = false,
    onConfirmClick: () -> Unit = {},
    onGrantClick: () -> Unit = {},
    onNoGrantClick: () -> Unit = {},
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding32, vertical = padding80)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.padding(padding16)
                ) {
                    Text(
                        text = if (showRationale) {
                            stringResource(deniedTitle)
                        } else stringResource(firstTimeTitle),
                        style = MaterialTheme.typography.titleMedium.copy(
                            lineBreak = LineBreak.Paragraph,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify,
                    )
                    Spacer(modifier = Modifier.height(space16))
                    if (isSelectable) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SelectButton(
                                textId = R.string.grant_label_button,
                                isConfirm = true
                            ) {
                                onGrantClick()
                            }
                            SelectButton(
                                textId = R.string.not_grant_label_button,
                            ) {
                                onNoGrantClick()
                            }
                        }
                    } else
                        SelectButton(
                            modifier = Modifier.align(Alignment.End),
                            textId = R.string.confirm_title,
                            isConfirm = true
                        ) {
                            onConfirmClick()
                        }
                }
            }
        }
    }
}

@Preview(locale = "ru")
@Composable
private fun AlertPermissionConfirmDialogPreview() {
    WeatherTheme {
        PermissionDialog(
            showRationale = false,
            deniedTitle = R.string.permission_location_denied_title,
            firstTimeTitle = R.string.permission_location_first_time_title,
        )
    }
}

@Preview(locale = "ru")
@Composable
private fun AlertPermissionSelectDialogPreview() {
    WeatherTheme {
        PermissionDialog(
            showRationale = false,
            isSelectable = true,
            deniedTitle = R.string.permission_notification_denied_title,
            firstTimeTitle = R.string.permission_notification_first_time_title,
        )
    }
}