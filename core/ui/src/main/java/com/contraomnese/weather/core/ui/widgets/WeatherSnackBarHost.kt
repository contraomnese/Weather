package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.contraomnese.weather.design.theme.padding20
import com.contraomnese.weather.design.theme.padding8

@Composable
fun WeatherSnackBarHost(snackBarHostState: SnackbarHostState) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = padding20)
    ) {
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = padding8)
                .zIndex(1f),
            hostState = snackBarHostState,
            snackbar = { snackBarData ->
                NotificationSnackBar(message = snackBarData.visuals.message)
            }
        )
    }
}