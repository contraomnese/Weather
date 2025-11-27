package com.contraomnese.weather.data.repository

import android.util.Log
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class LogMockerExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        mockkStatic(Log::class)

        justRun { Log.v(any(), any()) }
        justRun { Log.d(any(), any()) }
        justRun { Log.i(any(), any()) }
        justRun { Log.w(any<String>(), any<String>()) }
        justRun { Log.w(any<String>(), any<Throwable>()) }

        justRun { Log.wtf(any<String>(), any<String>()) }
        justRun { Log.wtf(any<String>(), any<Throwable>()) }
        justRun { Log.e(any(), any()) }
        justRun { Log.e(any(), any(), any()) }
        every { Log.d(any(), any()) } returns 0
    }

    override fun afterEach(context: ExtensionContext?) {
        unmockkStatic(Log::class)
    }
}