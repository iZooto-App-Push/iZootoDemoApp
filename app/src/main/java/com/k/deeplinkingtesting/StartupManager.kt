package com.k.deeplinkingtesting

import android.app.Application
import android.os.Trace
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class StartupManager(
    private val app: Application
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun initialize() {
        initCritical()

        deferNonCritical()
    }

    private fun initCritical() {

        Trace.beginSection("critical_startup")

        try {

            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )

            app.registerActivityLifecycleCallbacks(
                ActivityTracker()
            )
            scope.launch {
                PushSdkInitializer.init(app)
            }

        } finally {
            Trace.endSection()
        }
    }

    private fun deferNonCritical() {
        scope.launch {
            delay(1000)

            supervisorScope {

                launch {
                    runCatching {
                        FirebaseInitializer.init(app)
                    }
                }

                launch {
                    runCatching {
                        DeepLinkInitializer.init(app)
                    }
                }

                launch {
                    runCatching {
                        AdsInitializer.init(app)
                    }
                }
            }
        }
    }
}