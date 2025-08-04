package com.contraomnese.weather.design

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(name = "phone", showBackground = true, apiLevel = 35, backgroundColor = 0xFF1D4861)
@Preview(name = "darkPhone",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF1D4861, apiLevel = 35)
annotation class DevicePreviews()
