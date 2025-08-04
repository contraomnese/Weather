package com.contraomnese.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var keepSplash = true

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                return@setKeepOnScreenCondition keepSplash
            }

            setOnExitAnimationListener { splashScreenProvider ->
                splashScreenProvider.view.animate()
                    .alpha(0f)
                    .setDuration(1000L)
                    .withEndAction { splashScreenProvider.remove() }
                    .start()

                enableEdgeToEdge()
            }
        }

        lifecycleScope.launch {
            delay(500)
            keepSplash = false
        }

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WeatherTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {

    Scaffold { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            SearchTextField(
                searchQuery = "London",
                modifier = Modifier.padding(padding16)
            )
        }
    }
}