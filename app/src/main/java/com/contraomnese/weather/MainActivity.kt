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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.delay
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel

private const val LOTTIE_ASSET_NAME = "clear_sky.json"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { false }
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        actionBar?.hide()
        setContent {
            WeatherTheme {
                KoinAndroidContext {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(visible: Boolean, onFinish: () -> Unit) {

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(LOTTIE_ASSET_NAME))

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
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        onFinish()
    }
}

@Composable
internal fun WeatherApp(viewModel: MainActivityViewModel = koinViewModel()) {

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    Crossfade(targetState = uiState.isLoading, animationSpec = tween(1000)) { loading ->
        if (loading) {
            SplashScreen(uiState.isLoading) {
                viewModel.push(MainActivityAction.LottieAnimationFinished)
            }
        } else {
            WeatherHost(
                uiState = uiState,
                eventFlow = viewModel.eventFlow
            )
        }
    }
}
