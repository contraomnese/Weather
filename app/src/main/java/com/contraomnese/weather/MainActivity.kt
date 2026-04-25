package com.contraomnese.weather

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.navigation.WeatherHost
import com.contraomnese.weather.workers.setupWeatherSync
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        actionBar?.hide()
        setContent {
            WeatherTheme {
                KoinAndroidContext {
                    WeatherScreen(activateWeatherSync = { setupWeatherSync(this) })
                }
            }
        }
    }
}

@Composable
internal fun WeatherScreen(
    viewModel: MainActivityViewModel = koinViewModel(),
    activateWeatherSync: () -> Unit,
) {

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    Crossfade(targetState = uiState.isLoading, animationSpec = tween(1000)) { loading ->
        if (loading) {
            SplashScreen(uiState.isLoading) {
                viewModel.push(MainActivityAction.LottieAnimationFinished)
            }
        } else {
            if (uiState.forecastAutoSyncEnabled) activateWeatherSync()

            WeatherHost(
                startDestination = uiState.startDestination,
                eventFlow = viewModel.eventFlow
            )
        }
    }
}
