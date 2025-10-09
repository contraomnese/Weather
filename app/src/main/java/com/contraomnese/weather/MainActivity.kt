package com.contraomnese.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.navigation.WeatherHost
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext


class MainActivity : ComponentActivity() {

    private val viewModel by inject<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            WeatherTheme {
                KoinAndroidContext {
                    WeatherApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun SplashScreen(visible: Boolean, onFinish: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading_lottie.json"))

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        restartOnPlay = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            exit = scaleOut(animationSpec = tween(300))
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
        }
    }
    if (progress == 1f) {
        LaunchedEffect(Unit) { onFinish() }
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

    Crossfade(targetState = uiState.isLoading, animationSpec = tween(1000)) { loading ->
        if (loading) {
            SplashScreen(uiState.isLoading) { viewModel.onEvent(MainActivityEvent.NotLoading) }
        } else {
            WeatherHost(
                snackBarHostState = remember { SnackbarHostState() },
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
        }
    }
}
