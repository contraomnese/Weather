package com.contraomnese.weather.core.ui.widgets

import android.app.Activity
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.space8
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

@Composable
fun AlertGpsDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onGpsEnabled: (Boolean) -> Unit = {},
) {

    val context = LocalContext.current
    val activity = context as Activity

    val gpsNotActivated = stringResource(R.string.gps_not_activated_title)
    val gpsUnavailable = stringResource(R.string.gps_unavailable_title)
    val gpsDialogError = stringResource(R.string.gps_dialog_error_title)

    var gpsEnabled by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var intentSenderRequestToActivateGps by remember { mutableStateOf<IntentSenderRequest?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            gpsEnabled = true
            errorMessage = null
        } else {
            gpsEnabled = false
            errorMessage = gpsNotActivated
        }
    }

    LaunchedEffect(gpsEnabled) {
        onGpsEnabled(gpsEnabled)
    }

    LaunchedEffect(Unit) {
        checkGps(
            activity = activity,
            gpsUnavailable = gpsUnavailable,
            gpsDialogError = gpsDialogError,
            gpsEnabled = { enabled -> gpsEnabled = enabled },
            activateGps = { intentSenderRequest ->
                intentSenderRequestToActivateGps = intentSenderRequest

            },
            onError = { error ->
                errorMessage = error
            }
        )
    }

    AnimatedVisibility(
        visible = !gpsEnabled,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        GpsActivateDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            gpsEnabled = gpsEnabled,
            errorMessage = errorMessage,
            onClicked = {
                intentSenderRequestToActivateGps?.let {
                    launcher.launch(it)
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GpsActivateDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    gpsEnabled: Boolean = false,
    errorMessage: String? = null,
    onClicked: () -> Unit = {},
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
            Column(
                modifier = Modifier.padding(padding16),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space8)
            ) {

                Text(
                    text = stringResource(R.string.gps_alert_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onClicked,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.gps_activate_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Text(
                        text = when {
                            gpsEnabled -> stringResource(R.string.gps_enabled_title)
                            errorMessage != null -> errorMessage
                            else -> stringResource(R.string.gps_disabled_title)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }
}

private fun checkGps(
    activity: Activity,
    gpsUnavailable: String,
    gpsDialogError: String,
    gpsEnabled: (Boolean) -> Unit,
    activateGps: (IntentSenderRequest) -> Unit,
    onError: (String) -> Unit,
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        1000L
    ).build()

    val settingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .build()

    val settingsClient = LocationServices.getSettingsClient(activity)

    settingsClient.checkLocationSettings(settingsRequest)
        .addOnSuccessListener {
            gpsEnabled(true)
        }
        .addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    activateGps(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    onError(gpsUnavailable)
                }
            } else {
                onError(gpsDialogError)
            }
        }
}

@Preview(locale = "ru")
@Composable
private fun AlertGpsDialogPreview() {
    WeatherTheme {
        GpsActivateDialog()
    }
}

@Preview(locale = "ru", showSystemUi = true, showBackground = true, backgroundColor = 0xE81D6497)
@Composable
private fun BackgroundPreview() {
    WeatherTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}

@Preview(locale = "ru", showSystemUi = true, showBackground = true, backgroundColor = 0xFF2C72A1)
@Composable
private fun CurrentBackgroundPreview() {
    WeatherTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}

@Preview(locale = "ru", showSystemUi = true, showBackground = true, backgroundColor = 0xFF4B6C95)
@Composable
private fun OldBackgroundPreview() {
    WeatherTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}

@Preview(locale = "ru", showSystemUi = true, showBackground = true, backgroundColor = 0xFF1A5E9C)
@Composable
private fun NewBackgroundPreview() {
    WeatherTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}