package com.contraomnese.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.navigation.WeatherHost
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext

private const val SPLASHSCREEN_ANIMATION_DURATION = 1000L
private const val SPLASHSCREEN_ANIMATION_SCALE = 1.2f
private const val SPLASHSCREEN_ANIMATION_ALPHA = 0f

class MainActivity : ComponentActivity() {

    private val viewModel by inject<MainActivityViewModel>()

    private var keepSplashScreen = true

    init {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    keepSplashScreen = state.isLoading
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplashScreen
            }

            setOnExitAnimationListener { splashScreenProvider ->
                splashScreenProvider.view.animate()
                    .scaleX(SPLASHSCREEN_ANIMATION_SCALE)
                    .scaleY(SPLASHSCREEN_ANIMATION_SCALE)
                    .alpha(SPLASHSCREEN_ANIMATION_ALPHA)
                    .setDuration(SPLASHSCREEN_ANIMATION_DURATION)
                    .withEndAction { splashScreenProvider.remove() }
                    .start()
            }
        }
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WeatherTheme {
                KoinAndroidContext {
                    WeatherApp(viewModel)
                }
            }
        }
    }
}

@Composable
internal fun WeatherApp(viewModel: MainActivityViewModel) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.notificationEvents.collect { messageResId ->
            val message = context.getString(messageResId)
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    WeatherHost(uiState = uiState, snackBarHostState = snackBarHostState)
}